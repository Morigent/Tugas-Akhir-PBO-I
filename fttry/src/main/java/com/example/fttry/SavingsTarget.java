// SavingsTarget.java
package com.example.fttry;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class SavingsTarget {
    private int id;
    private String name;
    private double targetAmount;
    private double currentAmount;
    private LocalDate targetDate;
    private LocalDate createdAt;

    public SavingsTarget(String name, double targetAmount, LocalDate targetDate) {
        this.name = name;
        this.targetAmount = targetAmount;
        this.targetDate = targetDate;
        this.currentAmount = 0;
        this.createdAt = LocalDate.now();
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getTargetAmount() { return targetAmount; }
    public void setTargetAmount(double targetAmount) { this.targetAmount = targetAmount; }

    public double getCurrentAmount() { return currentAmount; }
    public void setCurrentAmount(double currentAmount) { this.currentAmount = currentAmount; }

    public LocalDate getTargetDate() { return targetDate; }
    public void setTargetDate(LocalDate targetDate) { this.targetDate = targetDate; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }

    // Hitung sisa bulan
    public int getRemainingMonths() {
        return (int) ChronoUnit.MONTHS.between(LocalDate.now(), targetDate);
    }

    // Hitung jumlah yang harus disisihkan per bulan
    public double getMonthlySaving() {
        int months = getRemainingMonths();
        return (targetAmount - currentAmount) / Math.max(1, months);
    }

    // Format progress
    public String getProgress() {
        double percentage = (currentAmount / targetAmount) * 100;
        return String.format("%.1f%% (Rp %,.0f dari Rp %,.0f)",
                percentage, currentAmount, targetAmount);
    }
}