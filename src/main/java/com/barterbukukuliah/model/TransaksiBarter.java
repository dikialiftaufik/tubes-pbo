package com.barterbukukuliah.model;

import java.sql.Timestamp;

public class TransaksiBarter {
    private int idTransaksi;
    private int idUserPengaju;
    private int idUserPemberi;
    private int idBukuDiminta;
    private int idBukuDitawarkan;
    private String status;
    private String pesanPengaju;
    private String pesanBalasan;
    private double matchScore;
    private Timestamp timestampRequest;
    private String judulBukuDiminta;
    private String judulBukuDitawarkan;
    private String namaPengaju;

    // Getter & Setter
    public int getIdTransaksi() { return idTransaksi; }
    public void setIdTransaksi(int idTransaksi) { this.idTransaksi = idTransaksi; }

    public int getIdUserPengaju() { return idUserPengaju; }
    public void setIdUserPengaju(int idUserPengaju) { this.idUserPengaju = idUserPengaju; }

    public int getIdUserPemberi() { return idUserPemberi; }
    public void setIdUserPemberi(int idUserPemberi) { this.idUserPemberi = idUserPemberi; }

    public int getIdBukuDiminta() { return idBukuDiminta; }
    public void setIdBukuDiminta(int idBukuDiminta) { this.idBukuDiminta = idBukuDiminta; }

    public int getIdBukuDitawarkan() { return idBukuDitawarkan; }
    public void setIdBukuDitawarkan(int idBukuDitawarkan) { this.idBukuDitawarkan = idBukuDitawarkan; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPesanPengaju() { return pesanPengaju; }
    public void setPesanPengaju(String pesanPengaju) { this.pesanPengaju = pesanPengaju; }

    public String getPesanBalasan() { return pesanBalasan; }
    public void setPesanBalasan(String pesanBalasan) { this.pesanBalasan = pesanBalasan; }

    public double getMatchScore() { return matchScore; }
    public void setMatchScore(double matchScore) { this.matchScore = matchScore; }

    public Timestamp getTimestampRequest() { return timestampRequest; }
    public void setTimestampRequest(Timestamp timestampRequest) { this.timestampRequest = timestampRequest; }

    public String getJudulBukuDiminta() {
        return judulBukuDiminta;
    }

    public void setJudulBukuDiminta(String judulBukuDiminta) {
        this.judulBukuDiminta = judulBukuDiminta;
    }

    public String getJudulBukuDitawarkan() {
        return judulBukuDitawarkan;
    }

    public void setJudulBukuDitawarkan(String judulBukuDitawarkan) {
        this.judulBukuDitawarkan = judulBukuDitawarkan;
    }

    public String getNamaPengaju() {
        return namaPengaju;
    }

    public void setNamaPengaju(String namaPengaju) {
        this.namaPengaju = namaPengaju;
    }
}
