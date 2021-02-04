package com.meama.security.auth;

import com.meama.common.security.UserDTO;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Set;

public class CustomGrantedAuthority implements GrantedAuthority {

    private String authority;
    private boolean active;
    private AuthType authType;
    private Set<String> permissions;
    private UserDTO user;
    private List<String> roles;

    public CustomGrantedAuthority() {
    }

    public CustomGrantedAuthority(boolean active) {
        this.active = active;
    }

    public CustomGrantedAuthority(String authority, Set<String> permissions) {
        this.authority = authority;
        this.permissions = permissions;
    }

    public CustomGrantedAuthority(String authority) {
        this.authority = authority;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public AuthType getAuthType() {
        return authType;
    }

    public void setAuthType(AuthType authType) {
        this.authType = authType;
    }

    @Override
    public String toString() {
        return "CustomGrantedAuthority{" +
                "authority='" + authority + '\'' +
                ", active=" + active +
                ", authType=" + authType +
                ", permissions=" + permissions +
                ", user=" + user +
                ", roles=" + roles +
                '}';
    }
}
