package com.barterbukukuliah.service;

import com.barterbukukuliah.dao.UserDAO;
import com.barterbukukuliah.model.User;

public class UserService {

    private final UserDAO userDAO = new UserDAO();

    /**
     * Registrasi user baru (role = "User").
     * Melakukan validasi: 
     * 1. Email & NIM tidak boleh sudah ada di database.
     * 2. Password dan konfirmasi sudah sesuai (logic di controller).
     * 3. Set atribut default (nama bisa kosong, trust_score=0.0, status_akun="Aktif", role="User").
     *
     * @param user objek User dengan setUser.getEmail(), getNim(), getPasswordHash() berisi plaintext password.
     * @throws Exception jika NIM/email sudah ada, atau SQL error.
     */
    public void register(User user) throws Exception {
        // 1. Cek duplikasi email
        if (userDAO.findByEmail(user.getEmail()) != null) {
            throw new Exception("Email sudah terdaftar.");
        }
        // 2. Cek duplikasi NIM
        if (userDAO.findByNim(user.getNim()) != null) {
            throw new Exception("NIM sudah terdaftar.");
        }

        // 3. Set atribut default
        user.setNama("");              // Nama bisa diisi nanti di Edit Profile
        user.setNomorTelepon("");
        user.setFakultas("");
        user.setProgramStudi("");
        user.setAngkatan(0);
        user.setAlamat("");
        user.setFotoProfil("");
        user.setTrustScore(0.0);
        user.setStatusAkun("Aktif");
        user.setRole("User");

        // 4. Insert user (password akan di‐hash di DAO)
        userDAO.insert(user);
    }
}
