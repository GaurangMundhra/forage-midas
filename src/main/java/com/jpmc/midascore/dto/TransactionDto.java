package com.jpmc.midascore.dto;

public class TransactionDto {
    private String senderName;
    private String recipientName;
    private Double amount;

    public TransactionDto() {}

    public TransactionDto(String senderName, String recipientName, Double amount) {
        this.senderName = senderName;
        this.recipientName = recipientName;
        this.amount = amount;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public Double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return String.format("TransactionDto[sender=%s, recipient=%s, amount=%.2f]", senderName, recipientName, amount);
    }
}
