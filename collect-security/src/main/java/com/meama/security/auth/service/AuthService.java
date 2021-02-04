package com.meama.security.auth.service;

import com.meama.security.exception.AuthException;

import java.util.Map;

public interface AuthService {

    Map<String, String> authorize(String username, String password) throws AuthException;

}
