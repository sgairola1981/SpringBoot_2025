package com.example.finance_agent.model;

import java.math.BigDecimal;

public class Voucher {

    private String voucherNo;
    private String hoa;
    private String partyName;
    private BigDecimal amount;
    private String status;

    public Voucher() {
    }

    public Voucher(String voucherNo,
                   String hoa,
                   String partyName,
                   BigDecimal amount,
                   String status) {

        this.voucherNo = voucherNo;
        this.hoa = hoa;
        this.partyName = partyName;
        this.amount = amount;
        this.status = status;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getHoa() {
        return hoa;
    }

    public void setHoa(String hoa) {
        this.hoa = hoa;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}