package com.barterbukukuliah.service;

import com.barterbukukuliah.model.Book;
import com.barterbukukuliah.model.User;

public class MatchingService {

    /**
     * Hitung skor matching (0-100) berdasarkan 5 kriteria:
     * 1. Mutual Interest (anggap true karena sudah request barter)
     * 2. Subject Match (mata kuliah sama)
     * 3. Condition Match (selisih kondisi max 1 level)
     * 4. Academic Level Match (angkatan max beda 2 tahun)
     * 5. Value Match (harga asli +-30%)
     */
    public static double hitungMatchScore(Book bukuDiminta, Book bukuDitawarkan, User pengaju, User pemberi) {
        int totalKriteria = 5;
        int matched = 0;

        // 1. Mutual Interest (anggap sudah benar)
        matched++;

        // 2. Subject Match
        if (bukuDiminta.getMataKuliah() != null && bukuDiminta.getMataKuliah().equalsIgnoreCase(bukuDitawarkan.getMataKuliah())) {
            matched++;
        }

        // 3. Condition Match
        if (cekKondisiMatch(bukuDiminta.getKondisi(), bukuDitawarkan.getKondisi())) {
            matched++;
        }

        // 4. Academic Level Match
        if (Math.abs(pengaju.getAngkatan() - pemberi.getAngkatan()) <= 2) {
            matched++;
        }

        // 5. Value Match
        double hargaDiminta = bukuDiminta.getHargaAsli();
        double hargaDitawarkan = bukuDitawarkan.getHargaAsli();
        if (hargaDitawarkan >= hargaDiminta * 0.7 && hargaDitawarkan <= hargaDiminta * 1.3) {
            matched++;
        }

        return (matched / (double) totalKriteria) * 100.0;
    }

    private static boolean cekKondisiMatch(String kondisiA, String kondisiB) {
        String[] levels = {"Baru", "Bagus", "Cukup", "Rusak Ringan", "Rusak Sedang"};
        int idxA = indexOfIgnoreCase(levels, kondisiA);
        int idxB = indexOfIgnoreCase(levels, kondisiB);
        if (idxA == -1 || idxB == -1) return false;
        return Math.abs(idxA - idxB) <= 1;
    }

    private static int indexOfIgnoreCase(String[] arr, String key) {
        if (key == null) return -1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equalsIgnoreCase(key)) return i;
        }
        return -1;
    }
}
