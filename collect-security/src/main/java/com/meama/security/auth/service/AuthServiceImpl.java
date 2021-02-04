package com.meama.security.auth.service;

import com.meama.common.SecurityUtils;
import com.meama.security.auth.JwtTokenProvider;
import com.meama.security.exception.AuthException;
import com.meama.security.messages.Messages;
import com.meama.security.user.service.UserService;
import com.meama.security.user.storage.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final UserService userService;

    public AuthServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Map<String, String> authorize(String username, String password) throws AuthException {
        User user = userService.getUserByUsername(username);
        if (user == null) {
            throw new AuthException(Messages.get("usernameOrPasswordIncorrect"));
        }
        if (!user.getActive()) {
            throw new AuthException(Messages.get("userIsNotActive"));
        }
        if (!SecurityUtils.PASSWORD_ENCODER.matches(password, user.getPassword())) {
            throw new AuthException(Messages.get("usernameOrPasswordIncorrect"));
        }
        Map<String, String> token = new HashMap<>();
        if (SecurityContextHolder.getContext() != null) {
            SecurityContextHolder.clearContext();
        }
        token.put("Authorization", JwtTokenProvider.generateUserToken(username));
        return token;
    }
}
