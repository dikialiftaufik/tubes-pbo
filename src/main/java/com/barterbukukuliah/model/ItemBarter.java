package com.barterbukukuliah.model;

/**
 * Kelas abstrak yang merepresentasikan item yang bisa dibarterkan.
 * Dapat digunakan untuk mewarisi struktur umum dari buku, barang, atau bentuk barter lainnya.
 */
public abstract class ItemBarter {
    protected String judul;
    protected String deskripsi;
    protected String kondisi;

    public ItemBarter() {}

    public ItemBarter(String judul, String deskripsi, String kondisi) {
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.kondisi = kondisi;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getKondisi() {
        return kondisi;
    }

    public void setKondisi(String kondisi) {
        this.kondisi = kondisi;
    }

    // Method abstrak yang harus diimplementasi subclass
    public abstract double hitungNilaiItem();
}
