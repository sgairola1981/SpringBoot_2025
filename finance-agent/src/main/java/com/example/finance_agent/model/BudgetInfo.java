package com.example.finance_agent.model;

import java.math.BigDecimal;

public class BudgetInfo {

    private String hoa;
    private boolean found;
    private BigDecimal availableBudget;
    private String message;

    public BudgetInfo() {
    }

    public BudgetInfo(String hoa,
                      boolean found,
                      BigDecimal availableBudget,
                      String message) {

        this.hoa = hoa;
        this.found = found;
        this.availableBudget = availableBudget;
        this.message = message;
    }

    public String getHoa() {
        return hoa;
    }

    public void setHoa(String hoa) {
        this.hoa = hoa;
    }

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public BigDecimal getAvailableBudget() {
        return availableBudget;
    }

    public void setAvailableBudget(BigDecimal availableBudget) {
        this.availableBudget = availableBudget;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}