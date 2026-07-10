package com.example.finance_agent.model;


public class InvestigationResult {

    private Voucher voucher;

    private WorkflowStatus workflow;

    private GstStatus gst;

    private BudgetInfo budget;

    private PaymentStatus payment;

    private String rootCause;

    private String recommendation;

    public Voucher getVoucher() {
        return voucher;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }

    public WorkflowStatus getWorkflow() {
        return workflow;
    }

    public void setWorkflow(WorkflowStatus workflow) {
        this.workflow = workflow;
    }

    public GstStatus getGst() {
        return gst;
    }

    public void setGst(GstStatus gst) {
        this.gst = gst;
    }

    public BudgetInfo getBudget() {
        return budget;
    }

    public void setBudget(BudgetInfo budget) {
        this.budget = budget;
    }

    public PaymentStatus getPayment() {
        return payment;
    }

    public void setPayment(PaymentStatus payment) {
        this.payment = payment;
    }

    public String getRootCause() {
        return rootCause;
    }

    public void setRootCause(String rootCause) {
        this.rootCause = rootCause;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

}