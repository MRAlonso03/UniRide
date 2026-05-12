package com.uniride.app.utils;

public class ValidationUtils {

    // Agrega aquí los dominios de correo de tu universidad
    private static final String[] ALLOWED_DOMAINS = {
            "@alumnos.udg.mx",
            "@cucei.udg.mx",
            // agrega el dominio de tu universidad aquí
    };

    public static boolean isInstitutionalEmail(String email) {
        if (email == null || email.isEmpty()) return false;
        email = email.toLowerCase().trim();
        for (String domain : ALLOWED_DOMAINS) {
            if (email.endsWith(domain)) return true;
        }
        return false;
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    public static boolean isValidName(String name) {
        return name != null && name.trim().length() >= 3;
    }

    public static boolean isValidStudentId(String studentId) {
        return studentId != null && !studentId.trim().isEmpty();
    }
}