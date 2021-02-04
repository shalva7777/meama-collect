package com.meama.security.user.storage;

import com.meama.common.security.UserDTO;
import com.meama.common.security.UserTypeDTO;
import com.meama.security.role.storage.RoleHelper;
import com.meama.security.user.storage.model.User;
import com.meama.security.user.storage.model.UserType;

import java.util.ArrayList;
import java.util.List;

public class UserHelper {

    public static User toEntity(UserDTO user) {
        if (user == null) {
            return null;
        }
        User result = new User();
        result.setId(user.getId());
        result.setUsername(user.getUsername());
        result.setPassword(user.getPassword());
        result.setFirstName(user.getFirstName());
        result.setLastName(user.getLastName());
        result.setEmail(user.getEmail());
        result.setPhone(user.getPhone());
        result.setPersonalId(user.getPersonalId());
        result.setCreationDate(user.getCreationDate());
        result.setModificationDate(user.getModificationDate());
        result.setActive(user.getActive());
        result.setChangePassword(user.getChangePassword());
        result.setImageUrl(user.getImageUrl());
        result.setUserType(fromUserTypeDTO(user.getUserType()));
        result.setRoles(RoleHelper.toEntities(user.getRoles()));
        return result;
    }

    public static UserDTO fromEntity(User user) {
        if (user == null) {
            return null;
        }
        UserDTO result = new UserDTO();
        result.setId(user.getId());
        result.setUsername(user.getUsername());
        result.setPassword(user.getPassword());
        result.setFirstName(user.getFirstName());
        result.setLastName(user.getLastName());
        result.setEmail(user.getEmail());
        result.setPhone(user.getPhone());
        result.setPersonalId(user.getPersonalId());
        result.setCreationDate(user.getCreationDate());
        result.setModificationDate(user.getModificationDate());
        result.setChangePassword(user.getChangePassword());
        result.setActive(user.getActive());
        result.setImageUrl("" + user.getImageUrl());
        result.setUserType(toUserTypeDTO(user.getUserType()));
        result.setRoles(RoleHelper.fromEntities(user.getRoles()));
        return result;
    }

    public static List<UserDTO> fromEntities(List<User> users) {
        if (users == null) {
            return null;
        }
        List<UserDTO> result = new ArrayList<>();
        for (User user : users) {
            result.add(fromEntity(user));
        }
        return result;
    }

    public static List<User> toEntities(List<UserDTO> users) {
        if (users == null) {
            return null;
        }
        List<User> result = new ArrayList<>();
        for (UserDTO userDTO : users) {
            result.add(toEntity(userDTO));
        }
        return result;
    }

    public static UserType fromUserTypeDTO(UserTypeDTO userType) {
        if (userType == null) {
            return null;
        }
        return UserType.valueOf(userType.name());
    }

    public static UserTypeDTO toUserTypeDTO(UserType userType) {
        if (userType == null) {
            return null;
        }
        return UserTypeDTO.valueOf(userType.name());
    }

    public static List<UserType> fromUserTypeDTOs(List<UserTypeDTO> userTypes) {
        if (userTypes == null) {
            return null;
        }
        List<UserType> result = new ArrayList<>();
        for (UserTypeDTO userType : userTypes) {
            result.add(fromUserTypeDTO(userType));
        }
        return result;
    }

    public static List<UserTypeDTO> toUserTypeDTOs(List<UserType> userTypes) {
        if (userTypes == null) {
            return null;
        }
        List<UserTypeDTO> result = new ArrayList<>();
        for (UserType userType : userTypes) {
            result.add(toUserTypeDTO(userType));
        }
        return result;
    }

}
