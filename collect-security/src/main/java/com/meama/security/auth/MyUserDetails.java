package com.meama.security.auth;

import com.meama.common.security.RoleDTO;
import com.meama.common.security.UserDTO;
import com.meama.security.messages.Messages;
import com.meama.security.user.service.UserService;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MyUserDetails implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(UserDetailsService.class);

    private final UserService userService;

    public MyUserDetails(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String token = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getHeader(JWTConstants.HEADER_STRING);
        AuthType userType = getUserType(token);
        if (userType == null) {
            logger.error("User name: " + (SecurityContextHolder.getContext().getAuthentication() != null ? SecurityContextHolder.getContext().getAuthentication().getName() : null) + " access token: " + token);
            throw new UsernameNotFoundException(Messages.get("invalidJWTToken"));
        }

        return loadSystemUserByUsername(username);


    }

//    private UserDetails loadGuestByPhone(String phone) throws UsernameNotFoundException {
//        final Tourist tourist;
//        tourist = TouristHelper.toEntity(this.touristService.getByPhone(phone));
//        if (tourist == null) {
//            throw new UsernameNotFoundException("User '" + phone + "' not found");
//        }
//        return org.springframework.security.core.userdetails.User
//                .withUsername(phone)
//                .password(tourist.getPhone())
//                .authorities(getTouristAuthorities(tourist))
//                .accountExpired(false)
//                .accountLocked(false)
//                .credentialsExpired(false)
//                .disabled(false)
//                .build();
//    }

    private UserDetails loadSystemUserByUsername(String username) throws UsernameNotFoundException {
        final UserDTO user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User '" + username + "' not found");
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(user.getPassword())
                .authorities(getAuthorities(username))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    private List<CustomGrantedAuthority> getAuthorities(String email) {
        UserDTO user = userService.findByUsername(email);
        if (user == null || !user.getActive()) {
            throw new UsernameNotFoundException("User is not active");
        }
        CustomGrantedAuthority role = new CustomGrantedAuthority(true);
        role.setAuthority("ROLE_" + AuthType.SYSTEM_USER.name());
        if (user.getRoles() != null) {
            role.setRoles(user.getRoles().stream().map(RoleDTO::getName).collect(Collectors.toList()));
        }
//        role.setPermissions(getPermissions(user.getRoles()));
        role.setUser(user);
        return Collections.singletonList(role);
    }

//    private Set<String> getPermissions(List<Role> roles) {
//        Set<String> permissions = new HashSet<>();
//        for (Role role : roles) {
//            for (Privilege privilege : role.getRolePrivileges()) {
//                for (CapApiUrl capApiUrl : privilege.getUrls()) {
//                    permissions.add(capApiUrl.getType().name() + " " + capApiUrl.getUrl());
//                }
//            }
//        }
//        return permissions;
//    }

//    private List<CustomGrantedAuthority> getTouristAuthorities(Tourist tourist) {
//        CustomGrantedAuthority role = new CustomGrantedAuthority(true);
////        role.setAuthType(AuthType.TOURIST);
//        role.setAuthority("ROLE_" + AuthType.TOURIST.name());
//        role.setTourist(tourist);
//        return Collections.singletonList(role);
//    }

    private AuthType getUserType(String token) {
        String type = Jwts.parser().setSigningKey(JWTConstants.SECRET).parseClaimsJws(token.replace(JWTConstants.TOKEN_PREFIX, "")).getBody().get(JWTConstants.USER_TYPE_KEY, String.class);
        if (type == null) return null;
        return AuthType.valueOf(type);
    }
}
