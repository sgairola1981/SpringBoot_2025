package com.example.finance_agent.model;

public class VoucherResponse {

    private boolean found;
    private String message;
    private Voucher voucher;

    public VoucherResponse() {
    }

    public VoucherResponse(boolean found, String message, Voucher voucher) {
        this.found = found;
        this.message = message;
        this.voucher = voucher;
    }

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Voucher getVoucher() {
        return voucher;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }
}