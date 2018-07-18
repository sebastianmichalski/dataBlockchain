package com.findwise.utils;

import com.google.gson.GsonBuilder;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

@Log4j2
public class StringUtil {

    public static String applySha256(@NonNull String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte aHash : hash) {
                String hex = Integer.toHexString(0xff & aHash);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            log.debug("There is no such algorithm: ", e);
            return "";
        }
    }

    public static String toGson(Collection c) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(c);
    }
}