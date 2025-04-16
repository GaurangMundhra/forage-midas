package com.jpmc.midascore.dto;

public class Incentive {

    private double amount;

    // No-arg constructor (required for deserialization)
    public Incentive() {
    }

    // Constructor with double parameter
    public Incentive(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
