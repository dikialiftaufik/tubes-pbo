package com.barterbukukuliah.util;

import java.util.regex.Pattern;

/**
 * Utilitas untuk memvalidasi input (email, password, dsb).
 */
public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    );

    private static final String[] ALLOWED_DOMAINS = {
            "@student.telkomuniversity.ac.id",
            "@tass.telkomuniversity.ac.id"
    };

    public static boolean isEmailFormatValid(String email) {
        if (email == null) return false;
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isTelkomDomain(String email) {
        if (email == null) return false;
        for (String domain : ALLOWED_DOMAINS) {
            if (email.toLowerCase().endsWith(domain)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPasswordStrong(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        boolean hasLetter = false;
        boolean hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            }
            if (hasLetter && hasDigit) {
                return true;
            }
        }
        return false;
    }
}
