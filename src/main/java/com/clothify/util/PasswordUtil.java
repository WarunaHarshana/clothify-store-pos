package com.clothify.util;

import lombok.experimental.UtilityClass;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

@UtilityClass
public class PasswordUtil {

    private static final int ITERATIONS = 120000;
    private static final int KEY_LENGTH = 256;

    public static String hashPassword(String plainPassword) {
        try {
            byte[] salt = new byte[16];
            SecureRandom.getInstanceStrong().nextBytes(salt);
            byte[] hash = pbkdf2(plainPassword.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            return ITERATIONS + ":" + Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verifyPassword(String plainPassword, String storedHash) {
        try {
            String[] parts = storedHash.split(":");
            if (parts.length != 3) {
                return plainPassword.equals(storedHash); // fallback for plain dev data
            }
            int iterations = Integer.parseInt(parts[0]);
            byte[] salt = Base64.getDecoder().decode(parts[1]);
            byte[] expected = Base64.getDecoder().decode(parts[2]);
            byte[] actual = pbkdf2(plainPassword.toCharArray(), salt, iterations, expected.length * 8);
            return MessageDigest.isEqual(expected, actual);
        } catch (Exception e) {
            return false;
        }
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int keyLength) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
        return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
    }
}

