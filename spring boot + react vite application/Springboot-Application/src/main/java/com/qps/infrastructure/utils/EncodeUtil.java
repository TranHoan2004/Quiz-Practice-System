package com.qps.infrastructure.utils;

import java.util.Base64;

public class EncodeUtil {
    public static String encode(String str) {
        return Base64.getUrlEncoder().encodeToString(str.getBytes());
    }

    public static String decode(String str) {
        return new String(Base64.getUrlDecoder().decode(str));
    }
}
