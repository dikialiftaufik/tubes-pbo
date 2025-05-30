package com.barterbukukuliah.model;

import java.time.LocalDateTime;

public class Book {
    private int idBuku;
    private int idPemilik;
    private String judul;
    private String penulis;
    private String isbn;
    private String mataKuliah;
    private String kondisi;
    private String deskripsi;
    private double hargaAsli;
    private String statusKetersediaan;
    private String fotoPath1;
    private String fotoPath2;
    private String fotoPath3;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Book() {
    }

    public int getIdBuku() {
        return idBuku;
    }

    public void setIdBuku(int idBuku) {
        this.idBuku = idBuku;
    }

    public int getIdPemilik() {
        return idPemilik;
    }

    public void setIdPemilik(int idPemilik) {
        this.idPemilik = idPemilik;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getPenulis() {
        return penulis;
    }

    public void setPenulis(String penulis) {
        this.penulis = penulis;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getMataKuliah() {
        return mataKuliah;
    }

    public void setMataKuliah(String mataKuliah) {
        this.mataKuliah = mataKuliah;
    }

    public String getKondisi() {
        return kondisi;
    }

    public void setKondisi(String kondisi) {
        this.kondisi = kondisi;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public double getHargaAsli() {
        return hargaAsli;
    }

    public void setHargaAsli(double hargaAsli) {
        this.hargaAsli = hargaAsli;
    }

    public String getStatusKetersediaan() {
        return statusKetersediaan;
    }

    public void setStatusKetersediaan(String statusKetersediaan) {
        this.statusKetersediaan = statusKetersediaan;
    }

    public String getFotoPath1() {
        return fotoPath1;
    }

    public void setFotoPath1(String fotoPath1) {
        this.fotoPath1 = fotoPath1;
    }

    public String getFotoPath2() {
        return fotoPath2;
    }

    public void setFotoPath2(String fotoPath2) {
        this.fotoPath2 = fotoPath2;
    }

    public String getFotoPath3() {
        return fotoPath3;
    }

    public void setFotoPath3(String fotoPath3) {
        this.fotoPath3 = fotoPath3;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}