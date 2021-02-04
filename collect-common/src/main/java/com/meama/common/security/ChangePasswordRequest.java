package com.meama.common.security;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ChangePasswordRequest {

    private String oldPassword;
    @NotNull
    @Size(min = 4, max = 50)
    private String newPassword;

    public ChangePasswordRequest() {
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
