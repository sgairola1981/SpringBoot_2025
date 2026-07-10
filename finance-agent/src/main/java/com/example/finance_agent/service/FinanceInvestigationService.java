package com.example.finance_agent.service;

import com.example.finance_agent.model.BudgetInfo;
import com.example.finance_agent.model.GstStatus;
import com.example.finance_agent.model.PaymentStatus;
import com.example.finance_agent.model.WorkflowStatus;
import com.example.finance_agent.tools.BudgetTool;
import com.example.finance_agent.tools.GstTool;
import com.example.finance_agent.tools.PaymentTool;
import com.example.finance_agent.tools.VoucherTool;
import com.example.finance_agent.tools.WorkflowTool;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class FinanceInvestigationService {

    private final VoucherTool voucherTool;
    private final WorkflowTool workflowTool;
    private final GstTool gstTool;
    private final BudgetTool budgetTool;
    private final PaymentTool paymentTool;

    /**
     * System prompt for the Enterprise Finance Investigation Agent.
     * You can pass this to your LLM client (Ollama/OpenAI/etc.) as the
     * "system" message.
     */
    public static final String SYSTEM_PROMPT = """
        You are an Enterprise Finance Investigation Agent.

        Rules:

        1. Never guess.
        2. Always use the available tools whenever required.
        3. Investigate in the following order:
           - Voucher
           - Workflow
           - GST
           - Budget
           - Payment

        4. Never expose:
           - SQL queries
           - SQL errors
           - Database exceptions
           - Java exceptions
           - Stack traces
           - Internal tool failures

        5. If a tool returns an exception or no data, simply display:
           "<Section>: Data not available."

        6. Never explain why data is unavailable.

        7. Never say:
           - SQL Grammar Error
           - PreparedStatement
           - Database Error
           - Exception occurred
           - Tool failed

        8. Show only business information.

        9. If the user's question asks only for voucher status,
           do not continue calling unnecessary tools after obtaining
           sufficient information.

        10. Call additional tools only when they are required to answer
            the user's question.

        Response Format:

        Voucher No : <voucher number>
        Status     : <voucher status>

        Workflow   : <Approved/Rejected/Pending/Data not available>

        GST        : <Matched/Pending/Data not available>

        Budget     : <Available/Exceeded/Data not available>

        Payment    : <Paid/Not Paid/Data not available>

        Return only the final formatted answer.
        """;

    public FinanceInvestigationService(
            VoucherTool voucherTool,
            WorkflowTool workflowTool,
            GstTool gstTool,
            BudgetTool budgetTool,
            PaymentTool paymentTool) {

        this.voucherTool = voucherTool;
        this.workflowTool = workflowTool;
        this.gstTool = gstTool;
        this.budgetTool = budgetTool;
        this.paymentTool = paymentTool;
    }

    /**
     * Main investigation entry: takes raw user input and returns the
     * final formatted answer (matching SYSTEM_PROMPT's Response Format).
     */
    public String investigate(String rawInput) {

        // 1. Clean user input to get actual voucher number
        String voucherNo = extractVoucherNumber(rawInput);
        if (voucherNo.isEmpty()) {
            return """
                    Voucher No : 
                    Status     : Data not available

                    Workflow   : Data not available
                    GST        : Data not available
                    Budget     : Data not available
                    Payment    : Data not available
                    """;
        }

        // 2. Voucher tool (first, mandatory)
        String voucherResult = voucherTool.getVoucherDetails(voucherNo);

        // If voucher doesn't exist, stop and show only voucher info
        if (voucherResult.contains("RESULT=NOT_FOUND")) {

            String voucherNumberFromTool = extractValue(voucherResult, "Voucher No");
            if (voucherNumberFromTool.isEmpty()) {
                voucherNumberFromTool = voucherNo;
            }

            String voucherStatus = extractValue(voucherResult, "Status");
            if (voucherStatus.isEmpty()) {
               // voucherStatus = "Data not available";
                return "Voucher " + voucherNo + " was not found.";
            }


            return """
                    Voucher No : %s
                    Status     : %s

                    Workflow   : Data not available
                    GST        : Data not available
                    Budget     : Data not available
                    Payment    : Data not available
                    """.formatted(
                    voucherNumberFromTool,
                    voucherStatus
            );
        }

        // 3. Voucher exists – parse fields
        String voucherNumberFromTool = extractValue(voucherResult, "Voucher No");
        if (voucherNumberFromTool.isEmpty()) {
            voucherNumberFromTool = voucherNo;
        }

        String voucherStatus = extractValue(voucherResult, "Status");
        if (voucherStatus.isEmpty()) {
            voucherStatus = "Data not available";
        }

        String hoa = extractValue(voucherResult, "HOA");

        // 4. Call other tools in parallel
        String finalVoucherNumberFromTool = voucherNumberFromTool;
        CompletableFuture<WorkflowStatus> workflowFuture =
                CompletableFuture.supplyAsync(() ->
                        workflowTool.getWorkflowStatus(finalVoucherNumberFromTool));

        String finalVoucherNumberFromTool1 = voucherNumberFromTool;
        CompletableFuture<GstStatus> gstFuture =
                CompletableFuture.supplyAsync(() ->
                        gstTool.checkGSTStatus(finalVoucherNumberFromTool1));

        CompletableFuture<BudgetInfo> budgetFuture =
                CompletableFuture.supplyAsync(() ->
                        budgetTool.checkBudget(hoa));

        String finalVoucherNumberFromTool2 = voucherNumberFromTool;
        CompletableFuture<PaymentStatus> paymentFuture =
                CompletableFuture.supplyAsync(() ->
                        paymentTool.paymentStatus(finalVoucherNumberFromTool2));

        CompletableFuture.allOf(
                workflowFuture,
                gstFuture,
                budgetFuture,
                paymentFuture
        ).join();

        WorkflowStatus workflow = safeJoin(workflowFuture);
        GstStatus gst = safeJoin(gstFuture);
        BudgetInfo budget = safeJoin(budgetFuture);
        PaymentStatus payment = safeJoin(paymentFuture);

        String workflowMessage = workflow != null && workflow.getMessage() != null
                ? workflow.getMessage()
                : "Data not available";

        String gstMessage = gst != null && gst.getMessage() != null
                ? gst.getMessage()
                : "Data not available";

        String budgetMessage = budget != null && budget.getMessage() != null
                ? budget.getMessage()
                : "Data not available";

        String paymentMessage = payment != null && payment.getMessage() != null
                ? payment.getMessage()
                : "Data not available";

        // 5. Final formatted answer (exactly as in SYSTEM_PROMPT)
        return """
                Voucher No : %s
                Status     : %s

                Workflow   : %s
                GST        : %s
                Budget     : %s
                Payment    : %s
                """.formatted(
                voucherNumberFromTool,
                voucherStatus,
                workflowMessage,
                gstMessage,
                budgetMessage,
                paymentMessage
        );
    }

    /**
     * Extracts a clean voucher number from raw user input.
     * Examples:
     * "Voucher 52859 status ?" -> "52859"
     * "52859" -> "52859"
     * "voucher no 12345" -> "12345"
     */
    private String extractVoucherNumber(String rawInput) {
        if (rawInput == null) {
            return "";
        }

        String trimmed = rawInput.trim();
        trimmed = trimmed.replaceFirst("(?i)^voucher\\s+", "");

        String[] parts = trimmed.split("\\s+");
        if (parts.length == 0) {
            return "";
        }

        String firstToken = parts[0];

        // If the first token looks like a voucher (letters+digits),
        // just use it as-is.
        if (firstToken.matches("[A-Za-z0-9]+")) {
            return firstToken;
        }

        // Fallback: digits only from entire input (for weird formats)
        String digitsOnly = trimmed.replaceAll("\\D", "");
        return digitsOnly;
    }
    /**
     * Extracts a value from a multiline text of form:
     * KEY=value
     */
    private String extractValue(String text, String key) {
        if (text == null || key == null) {
            return "";
        }

        String keyLower = key.toLowerCase();

        for (String line : text.split("\\R")) {
            String trimmed = line.trim();
            int idx = trimmed.toLowerCase().indexOf(keyLower + "=");
            if (idx == 0) {
                String valuePart = trimmed.substring(key.length() + 1); // +1 for '='
                return valuePart.trim();
            }
        }
        return "";
    }

    /**
     * Safe join that avoids leaking exceptions; any error is treated
     * as "Data not available" by returning null.
     */
    private <T> T safeJoin(CompletableFuture<T> future) {
        try {
            return future.join();
        } catch (Exception e) {
            return null;
        }
    }

}