package com.meama.meamacollect.application.controller.security;

import com.meama.common.security.UserDTO;
import com.meama.common.security.request.LoginRequest;
import com.meama.security.auth.JWTConstants;
import com.meama.security.auth.service.AuthService;
import com.meama.security.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping(path = "api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @Autowired
    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping(value = "/authorize")
    public Map<String, String> authorize(@RequestBody LoginRequest request) throws Exception {
        return authService.authorize(request.getUsername(), request.getPassword());
    }

    @GetMapping(value = "/authorize")
    public boolean authorize(@RequestHeader HttpHeaders headers) {
        return headers.get(JWTConstants.HEADER_STRING) != null;
    }

    @GetMapping(value = "/authorized-user")
    public UserDTO authorizedUser() throws Exception {
        return userService.getAuthorizedUser();
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public void logoutPage(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        SecurityContextHolder.clearContext();
        session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        for (Cookie cookie : request.getCookies()) {
            cookie.setMaxAge(0);
        }
    }
}
