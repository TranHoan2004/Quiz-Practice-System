package utils;

import lombok.Getter;

public class TokenUtils {
    private static String token;
    private static long expirationTime;

    public static void setToken(String newToken, long expiration) {
        token = newToken;
        expirationTime = System.currentTimeMillis() + expiration;
    }

    public static String getToken() {
        if (System.currentTimeMillis() > expirationTime) {
            token = null;
            expirationTime = 0;
        }
        return token;
    }

}
