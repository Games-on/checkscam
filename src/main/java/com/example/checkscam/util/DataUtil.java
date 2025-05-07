package com.example.checkscam.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class DataUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

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

    public static String extractFullDomain(String url) {
        if (url == null || url.trim().isEmpty()) {
            return null;
        }

        try {
            // Thêm scheme nếu thiếu
            if (!url.matches("^(?i)(http|https|ftp)://.*")) {
                url = "http://" + url;
            }

            URL parsedUrl = new URL(url);
            String host = parsedUrl.getHost();

            if (host != null && host.startsWith("www.")) {
                host = host.substring(4); // Bỏ "www." nếu muốn
            }

            return host;
        } catch (Exception e) {
            return null;
        }
    }

}
