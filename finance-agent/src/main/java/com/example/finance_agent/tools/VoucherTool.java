package com.example.finance_agent.tools;

import com.example.finance_agent.model.Voucher;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class VoucherTool {

    private final JdbcTemplate jdbcTemplate;

    public VoucherTool(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Tool(
            name = "getVoucherDetails",
            description = "Returns voucher details using voucher number."
    )
    public String getVoucherDetails(String voucherNo) {

        String sql = """
                SELECT
                    VOUCHER_NO AS voucherNo,
                    HOA AS hoa,
                    PARTY_NAME AS partyName,
                    AMOUNT AS amount,
                    STATUS AS status
                FROM FA_VOUCHER
                WHERE VOUCHER_NO = ?
                """;

        try {

            Voucher voucher = jdbcTemplate.queryForObject(
                    sql,
                    new BeanPropertyRowMapper<>(Voucher.class),
                    voucherNo
            );

            // Voucher found – business information only
            return """
                    RESULT=FOUND

                    Voucher No=%s
                    HOA=%s
                    Party Name=%s
                    Amount=%s
                    Status=%s

                    IMPORTANT:
                    Voucher exists.
                    Continue investigation using Workflow, GST, Budget and Payment tools.
                    """
                    .formatted(
                            voucher.getVoucherNo(),
                            voucher.getHoa(),
                            voucher.getPartyName(),
                            voucher.getAmount(),
                            voucher.getStatus()
                    );

        } catch (EmptyResultDataAccessException ex) {

            // No voucher row – do not leak SQL/internal errors
            return """
                    RESULT=NOT_FOUND

                    Voucher No=%s

                    IMPORTANT:
                    Voucher does not exist.
                    Stop investigation.
                    Do NOT call any other tool.

                    FINAL ANSWER:
                    Voucher %s was not found.
                    """
                    .formatted(voucherNo, voucherNo);
        }
    }
}