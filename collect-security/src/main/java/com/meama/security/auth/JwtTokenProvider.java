package com.meama.security.auth;

import com.meama.security.exception.SecurityException;
import com.meama.security.messages.Messages;
import com.meama.security.user.storage.model.UserType;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    private final MyUserDetails myUserDetails;
    private Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Autowired
    public JwtTokenProvider(MyUserDetails myUserDetails) {
        this.myUserDetails = myUserDetails;
    }

    public static String generateUserToken(String username) {
        Map<String, Object> map = new HashMap<>();
        map.put(JWTConstants.USER_TYPE_KEY, UserType.SYSTEM_USER.name());
        map.put(JWTConstants.SUBJECT, username);
        return JWTConstants.TOKEN_PREFIX + " " + Jwts.builder()
                .setClaims(map)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWTConstants.SYSTEM_USER_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, JWTConstants.SECRET)
                .compact();
    }

//    public static String generateTouristToken(String phone) {
//        Map<String, Object> map = new HashMap<>();
//        map.put(JWTConstants.USER_TYPE_KEY, AuthType.TOURIST.name());
//        map.put(JWTConstants.SUBJECT, phone);
//        return JWTConstants.TOKEN_PREFIX + " " + Jwts.builder()
//                .setClaims(map)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + JWTConstants.TOURIST_EXPIRATION_TIME))
//                .signWith(SignatureAlgorithm.HS512, JWTConstants.SECRET)
//                .compact();
//
//    }

    public Authentication getAuthentication(String token) throws SecurityException {
        String username = getUsername(token);
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = myUserDetails.loadUserByUsername(username);
            return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        } else {
            if (SecurityContextHolder.getContext().getAuthentication().getName().equals(username)) {
                return SecurityContextHolder.getContext().getAuthentication();
            }
        }
        throw new SecurityException(Messages.get("invalidJWTToken"), HttpStatus.NOT_ACCEPTABLE);
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(JWTConstants.SECRET).parseClaimsJws(token).getBody().get(JWTConstants.SUBJECT, String.class);
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader(JWTConstants.HEADER_STRING);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    public boolean validateToken(String token) throws SecurityException {
        try {
            Jwts.parser()
                    .setSigningKey(JWTConstants.SECRET)
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
             throw new SecurityException(Messages.get("invalidJWTToken"), HttpStatus.UNAUTHORIZED);
        }
    }

}
