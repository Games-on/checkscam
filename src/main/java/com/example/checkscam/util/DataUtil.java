package com.example.checkscam.util;

public class DataUtil {

    public static String normalizePhoneNumber(String input) {
        if (input == null) {
            return null;
        }

        String phone = input.trim()
                .replaceAll("\\s+", "")
                .replaceAll("\\.", "");

        if (phone.startsWith("+84")) {
            phone = "0" + phone.substring(3);
        }

        return phone;
    }

    public static boolean isValidPhoneNumber(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }

        String normalized = normalizePhoneNumber(input);

        return normalized.matches("^0\\d{9}$");
    }

    public static boolean isValidBankAccount(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }

        String account = input.trim();

        return account.matches("^\\d{8,16}$");
    }
}
