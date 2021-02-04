package com.meama.common.security;

import java.io.Serializable;
import java.util.Objects;

public class PrivilegeDTO implements Serializable {

    private Long id;
    private String code;
    private String name;
    private String groupName;

    public PrivilegeDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PrivilegeDTO)) return false;
        PrivilegeDTO privilege = (PrivilegeDTO) o;
        return this.id == privilege.getId() && this.code.equals(privilege.getCode())
                && this.getName().equals(privilege.getName()) && this.getGroupName().equals(privilege.getGroupName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, groupName);
    }
}
