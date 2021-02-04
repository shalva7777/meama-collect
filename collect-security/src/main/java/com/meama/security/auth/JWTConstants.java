package com.meama.security.auth;

public class JWTConstants {
    //TODO secret must be generated from keystore [AB]
    public static final String SECRET = "aTNXkVG9123ICl0uAm0l0ta8wIQQJc";
    public static final String TOKEN_PREFIX = "Bearer";
    public static final String HEADER_STRING = "Authorization";
    public static final long SYSTEM_USER_EXPIRATION_TIME = 3_600_000; // 60 minutes
    public static final long TOURIST_EXPIRATION_TIME = 2 * 3_600_000; // 60 minutes
    public static final String USER_TYPE_KEY = "type";
    public static final String SUBJECT = "subject";
}
