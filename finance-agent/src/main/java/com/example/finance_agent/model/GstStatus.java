package com.example.finance_agent.model;

public class GstStatus {

    private String voucherNo;
    private boolean found;
    private boolean completed;
    private String message;

    public GstStatus() {
    }

    public GstStatus(String voucherNo,
                     boolean found,
                     boolean completed,
                     String message) {

        this.voucherNo = voucherNo;
        this.found = found;
        this.completed = completed;
        this.message = message;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}