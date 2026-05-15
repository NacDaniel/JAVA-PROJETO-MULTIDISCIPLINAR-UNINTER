package com.danielnac.multidisciplinar.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHelper {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String encrypt(String senha) {
        return encoder.encode(senha);
    }

    public static boolean validate(String senha, String hashBanco) {
        return encoder.matches(senha, hashBanco);
    }
}
