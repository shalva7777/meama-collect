package com.meama.common;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SecurityUtils {

    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
}
