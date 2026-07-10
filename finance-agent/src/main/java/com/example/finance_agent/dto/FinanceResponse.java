package com.example.finance_agent.dto;

public class FinanceResponse {

    private String voucherNo;
    private String voucherStatus;
    private String workflow;
    private String gst;
    private String budget;
    private String payment;
    private long executionMs;
    private boolean voucherFound; // NEW flag

    public FinanceResponse() {
    }

    public FinanceResponse(String voucherNo,
                           String voucherStatus,
                           String workflow,
                           String gst,
                           String budget,
                           String payment,
                           long executionMs,
                           boolean voucherFound) {
        this.voucherNo = voucherNo;
        this.voucherStatus = voucherStatus;
        this.workflow = workflow;
        this.gst = gst;
        this.budget = budget;
        this.payment = payment;
        this.executionMs = executionMs;
        this.voucherFound = voucherFound;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getVoucherStatus() {
        return voucherStatus;
    }

    public void setVoucherStatus(String voucherStatus) {
        this.voucherStatus = voucherStatus;
    }

    public String getWorkflow() {
        return workflow;
    }

    public void setWorkflow(String workflow) {
        this.workflow = workflow;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public long getExecutionMs() {
        return executionMs;
    }

    public void setExecutionMs(long executionMs) {
        this.executionMs = executionMs;
    }

    public boolean isVoucherFound() {
        return voucherFound;
    }

    public void setVoucherFound(boolean voucherFound) {
        this.voucherFound = voucherFound;
    }
}