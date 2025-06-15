// src/main/java/com/barterbukukuliah/model/TrustScoreHistory.java
package com.barterbukukuliah.model;

import java.time.LocalDateTime;

public class TrustScoreHistory {
    private String actionType;
    private double scoreChange;
    private double oldScore; 
    private double newScore;
    private String keterangan;
    private LocalDateTime createdAt;
    private Integer referenceId; 

    // Konstruktor bisa ditambahkan jika perlu
    public TrustScoreHistory() {}

    // Getters and Setters
    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public double getScoreChange() {
        return scoreChange;
    }

    public void setScoreChange(double scoreChange) {
        this.scoreChange = scoreChange;
    }

    public double getOldScore() {
        return oldScore;
    }

    public void setOldScore(double oldScore) {
        this.oldScore = oldScore;
    }

    public double getNewScore() {
        return newScore;
    }

    public void setNewScore(double newScore) {
        this.newScore = newScore;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }
}