package controller;

public class Transaction {

    private Double amount;
    private String type;
    private Long parentId;

    public Transaction() {
    }

    public Transaction(Double amount, String type, Long parentId) {
        this.amount = amount;
        this.type = type;
        this.parentId = parentId;
    }

    public Double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
