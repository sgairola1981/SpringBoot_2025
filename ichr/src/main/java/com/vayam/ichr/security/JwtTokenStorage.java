package com.vayam.ichr.security;

import org.springframework.stereotype.Component;

@Component
public class JwtTokenStorage {
    private static String token;

    public static String getToken() {
        return token;
    }

    public static void setToken(String newToken) {
        token = newToken;
    }
}
