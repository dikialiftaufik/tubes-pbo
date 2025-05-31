package com.barterbukukuliah.service;

import com.barterbukukuliah.dao.UserDAO;
import com.barterbukukuliah.model.User;
import com.barterbukukuliah.util.HashUtil;

public class AuthService {

    private final UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    public User login(String email, String password) throws Exception {
        System.out.println("DEBUG [AuthService.login] Mencari user dengan email: " + email);
        User user = userDAO.findByEmail(email);
        if (user == null) {
            System.out.println("DEBUG [AuthService.login] Tidak ditemukan user dengan email: " + email);
            throw new Exception("Email tidak terdaftar.");
        }
        System.out.println("DEBUG [AuthService.login] User ditemukan, id=" + user.getIdUser() + ", role=" + user.getRole());
        System.out.println("DEBUG [AuthService.login] Hash yang tersimpan di DB: " + user.getPasswordHash());

        if (!"Aktif".equalsIgnoreCase(user.getStatusAkun())) {
            System.out.println("DEBUG [AuthService.login] Status akun user bukan Aktif: " + user.getStatusAkun());
            throw new Exception("Akun Anda tidak aktif. Silakan hubungi admin.");
        }

        boolean passwordMatch;
        try {
            passwordMatch = HashUtil.checkPassword(password, user.getPasswordHash());
        } catch (Exception e) {
            System.out.println("DEBUG [AuthService.login] Error saat memeriksa password: " + e.getMessage());
            throw new Exception("Terjadi kesalahan pada saat memeriksa password.");
        }
        System.out.println("DEBUG [AuthService.login] Hasil checkPassword: " + passwordMatch);

        if (!passwordMatch) {
            throw new Exception("Password salah.");
        }

        System.out.println("DEBUG [AuthService.login] Login berhasil untuk user id=" + user.getIdUser());
        return user;
    }
}
