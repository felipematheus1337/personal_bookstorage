package com.bookstorage.app.utils;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BusinessHelperUtil {

    public static final String BUCKET_NAME = "bookstorage";

    public static String normalizeFileName(String fileName, String id) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HH");
        String timestamp = LocalDateTime.now().format(formatter);
        StringBuilder sb = new StringBuilder();
        sb.append(fileName);
        sb.append(" ");
        sb.append("id_");
        sb.append(timestamp);
        return sb.toString();
    }
}
