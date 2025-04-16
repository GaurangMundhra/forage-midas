package com.jpmc.midascore.entity;


import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
public class TransactionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private UserRecord sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private UserRecord recipient;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private Double incentive;

    protected TransactionRecord() {}

    // ✅ Existing constructor without incentive
    public TransactionRecord(UserRecord sender, UserRecord recipient, Double amount) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.incentive = 0.0;
        this.timestamp = LocalDateTime.now();
    }

    // ✅ New constructor with incentive
    public TransactionRecord(UserRecord sender, UserRecord recipient, Double amount, Double incentive) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.incentive = incentive;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public UserRecord getSender() {
        return sender;
    }

    public UserRecord getRecipient() {
        return recipient;
    }

    public Double getAmount() {
        return amount;
    }

    public Double getIncentive() {
        return incentive;
    }

    public void setIncentive(Double incentive) {
        this.incentive = incentive;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format(
                "Transaction[id=%d, sender=%s, recipient=%s, amount=%.2f, incentive=%.2f, timestamp=%s]",
                id, sender.getName(), recipient.getName(), amount, incentive, timestamp);
    }
}

