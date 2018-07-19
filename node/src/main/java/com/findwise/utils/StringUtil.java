package com.findwise.utils;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;

import static com.google.common.hash.Hashing.sha256;

@Log4j2
public class StringUtil {

    private StringUtil() {
    }

    public static String applySha256(@NonNull String input) {
        return sha256()
                .hashString(input, StandardCharsets.UTF_8)
                .toString();
    }
}