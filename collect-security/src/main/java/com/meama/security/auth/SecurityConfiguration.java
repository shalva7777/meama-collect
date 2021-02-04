package com.meama.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public SecurityConfiguration(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/api/auth/authorize")
                .antMatchers("/api/auth/logout")
                .antMatchers("/application/upload-api/**");
        super.configure(web);
    }

    @Override
    @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/bower_components/**", "/login", "/*.css", "/*.js", "*.bundle.*", "/main.css", "/fonts/**",
                        "/images/**", "/assets/**", "/*.woff", "/*.ttf", "/*.woff2",
                        "/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**",
                        "/webjars/**",
                        "/api/auth/authorize",
                        "/api/auth/logout",
                        "/application/upload-api/**",
                        "/error"
                )
                .permitAll()
                .antMatchers("/**").hasRole(AuthType.SYSTEM_USER.name())
                .and()
                .formLogin()
                .permitAll()
                .and()
                .httpBasic()
                .and().exceptionHandling()
                .authenticationEntryPoint(unauthorizedEntryPoint())
                .and()
                .csrf().disable()
                .cors();
        http.apply(new JwtTokenFilterConfigurer(jwtTokenProvider));
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedOrigins("*")
                        .allowedHeaders("*");
            }
        };
    }

    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) -> {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        };
    }
}
