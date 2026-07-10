package com.example.finance_agent.model;

import java.util.Date;

public class PaymentStatus {

    private String voucherNo;
    private boolean found;
    private boolean paid;
    private Date paymentDate;
    private String message;

    public PaymentStatus() {
    }

    public PaymentStatus(String voucherNo, boolean found, boolean paid,
                         Date paymentDate, String message) {
        this.voucherNo = voucherNo;
        this.found = found;
        this.paid = paid;
        this.paymentDate = paymentDate;
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

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}