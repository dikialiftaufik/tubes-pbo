package com.barterbukukuliah.model;

import java.sql.Timestamp;

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
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private User pemilik;

    // **FIELD TAMBAHAN untuk menampung nama pemilik (di‐JOIN dari tabel users)**
    private String pemilikName;

    // … Getter/Setter untuk semua field di atas … //

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

    public Timestamp getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    // --- Getter/Setter untuk field tambahan pemilikName ---
    public String getPemilikName() {
        return pemilik != null ? pemilik.getNama() : (pemilikName != null ? pemilikName : "");
    }
    
    public void setPemilikName(String pemilikName) {
        this.pemilikName = pemilikName;
    }

    @Override
    public String toString() {
        return judul; // Masih menampilkan judul saat misalnya dipakai dalam ChoiceDialog
    }

    public User getPemilik() {
        return pemilik;
    }
    
    public void setPemilik(User pemilik) {
        this.pemilik = pemilik;
    }
}
