package com.jpmc.midascore.foundation;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L; // Required for Kafka serialization

    private long transactionId;
    private String sender;    // Changed from senderId to sender (String for usernames)
    private String recipient; // Changed from recipientId to recipient (String for usernames)
    private BigDecimal amount; // Changed from float to BigDecimal

    public Transaction() {
    }

    public Transaction(long transactionId, String sender, String recipient, BigDecimal amount) {
        this.transactionId = transactionId;
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transaction { transactionId=" + transactionId + 
               ", sender='" + sender + '\'' + 
               ", recipient='" + recipient + '\'' + 
               ", amount=" + amount + " }";
    }
}
