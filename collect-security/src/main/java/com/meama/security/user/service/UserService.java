package com.meama.security.user.service;

import com.meama.common.response.ComboObject;
import com.meama.common.response.ListResult;
import com.meama.common.security.UserDTO;
import com.meama.common.security.UserTypeDTO;
import com.meama.security.user.storage.model.User;

import java.util.Date;
import java.util.List;

public interface UserService {

    User getUserFromContext() throws SecurityException;

    UserDTO getAuthorizedUser() throws SecurityException;

    ListResult<UserDTO> find(String query, int limit, int offset, String orderBy, boolean asc);

    ListResult<UserDTO> find(Long id, String username, String firstName, String lastName, String sapCode, String email, String phone, String personalId, Boolean isActive,
                             Date fromCreationDate, Date toCreationDate, Date fromModificationDate, Date toModificationDate, Long roleId, UserTypeDTO userType, Long companyID,
                             Integer fromRating, Integer toRating, int limit, int offset, String orderBy, boolean asc);

    UserDTO update(UserDTO user) throws SecurityException;

    UserDTO save(UserDTO user) throws SecurityException;

    User findByEmail(String email);

    UserDTO findByUsername(String email);

    void changePassword(String oldPassword, String newPassword) throws SecurityException;

    UserDTO getUserById(Long id) throws SecurityException;

    List<ComboObject> getUserTypes();

    User getUserByUsername(String name);

    UserDTO activate(long id);

    UserDTO deactivate(long id);

    void delete(long id);

    void changeUserPassword(Long userId, String newPassword) throws SecurityException;

    List<String> usersByRole(String role);

    ListResult<UserDTO> findUsersWithoutDepartment(String query, int limit, int offset);

    User getActiveUserByUsername(String name) throws SecurityException;
}
