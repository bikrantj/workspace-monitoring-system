package com.bikrantj.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;

public class PasswordUtil {
    private static final SecureRandom random = new SecureRandom();

    /**
     * Generates a random salt (16 bytes, hex-encoded).
     */
    public static String generateSalt() {
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return HexFormat.of().formatHex(salt);
    }

    /**
     * Hashes the password using SHA-256 with the provided salt.
     */
    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] hashedBytes = md.digest(password.getBytes());
            return HexFormat.of().formatHex(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    /**
     * Verifies a plain password against the stored hash and salt.
     */
    public static boolean verifyPassword(String plainPassword, String salt, String storedHash) {
        String hashedInput = hashPassword(plainPassword, salt);
        return hashedInput.equals(storedHash);
    }
}