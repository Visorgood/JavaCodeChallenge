package controller;

public class Transaction {

    private final double amount;
    private final String type;
    private final long parentId;

    public Transaction(double amount, String type, long parentId) {
        this.amount = amount;
        this.type = type;
        this.parentId = parentId;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public long getParentId() {
        return parentId;
    }
}
