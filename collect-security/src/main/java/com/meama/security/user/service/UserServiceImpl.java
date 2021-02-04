package com.meama.security.user.service;

import com.meama.common.SecurityUtils;
import com.meama.common.response.ComboObject;
import com.meama.common.response.ListResult;
import com.meama.common.security.PrivilegeDTO;
import com.meama.common.security.RoleDTO;
import com.meama.common.security.UserDTO;
import com.meama.common.security.UserTypeDTO;
import com.meama.security.messages.Messages;
import com.meama.security.user.storage.UserHelper;
import com.meama.security.user.storage.UserRepository;
import com.meama.security.user.storage.UserStorage;
import com.meama.security.user.storage.model.User;
import com.meama.security.user.storage.model.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository repository;
    private final UserStorage userStorage;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserStorage userStorage) {
        this.repository = userRepository;
        this.userStorage = userStorage;
    }

    @Override
    public User getUserFromContext() throws SecurityException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = repository.getUserByUsername(authentication.getName());
        if (user == null) {
            SecurityContextHolder.clearContext();
            throw new SecurityException(Messages.get("AccessDenied"));
        }
        return user;
    }

    @Override
    public UserDTO getAuthorizedUser() throws SecurityException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDTO user = UserHelper.fromEntity(repository.getUserByUsername(authentication.getName()));
        if (user == null) {
            SecurityContextHolder.clearContext();
            throw new SecurityException(Messages.get("AccessDenied"));
        }
        Set<PrivilegeDTO> uniquePrivileges = new HashSet<>();
        for (RoleDTO role : user.getRoles()) {
            uniquePrivileges.addAll(role.getRolePrivileges());
        }
        Map<String, List<String>> map = new HashMap<>();
        for (PrivilegeDTO privilege : uniquePrivileges) {
            if (!map.containsKey(privilege.getGroupName())) {
                map.put(privilege.getGroupName(), new ArrayList<>());
            }
            map.get(privilege.getGroupName()).add(privilege.getCode());
        }
        user.setPrivileges(map);
        return user;
    }

    @Override
    public ListResult<UserDTO> find(String query, int limit, int offset, String orderBy, boolean asc) {
        ListResult<User> users = userStorage.find(query, limit, offset, orderBy, asc);
        ListResult<UserDTO> result = users.copy(UserDTO.class);
        result.setResultList(UserHelper.fromEntities(users.getResultList()));
        return result;
    }

    @Override
    public ListResult<UserDTO> find(Long id, String username, String firstName, String lastName, String sapCode, String email, String phone, String personalId, Boolean isActive,
                                    Date fromCreationDate, Date toCreationDate, Date fromModificationDate, Date toModificationDate,
                                    Long roleId, UserTypeDTO userType, Long companyId, Integer fromRating, Integer toRating, int limit, int offset, String orderBy, boolean asc) {
        ListResult<User> users = userStorage.findByFilters(id, username, firstName, lastName, sapCode, email, phone, personalId, isActive, fromCreationDate, toCreationDate,
                fromModificationDate, toModificationDate, roleId, UserHelper.fromUserTypeDTO(userType), fromRating, toRating, limit, offset, orderBy, asc, companyId);
        ListResult<UserDTO> result = users.copy(UserDTO.class);
        result.setResultList(UserHelper.fromEntities(users.getResultList()));
        return result;
    }

    @Override
    public UserDTO update(UserDTO userDTO) throws SecurityException {
        User prevUser = repository.findOne(userDTO.getId());
        if (prevUser == null) {
            throw new SecurityException(Messages.get("objectNotFound"));
        }
        if (!prevUser.getUsername().equals(userDTO.getUsername()) &&
                (repository.getUserByUsername(userDTO.getUsername()) != null)) {
            throw new SecurityException(Messages.get("usernameAlreadyExists"));
        }
//        if (!prevUser.getPersonalId().equals(userDTO.getPersonalId()) &&
//                (repository.getUserByPersonalId(userDTO.getPersonalId()) != null)) {
//            throw new SecurityException(Messages.get("personalIdAlreadyExists"));
//        }
//        if (prevUser.getImageUrl() != null && userDTO.getImageUrl() != null && !prevUser.getImageUrl().equals(userDTO.getImageUrl())) {
//            storageService.deleteImage(prevUser.getImageUrl());
//        }
        userDTO.setPassword(prevUser.getPassword());
        userDTO.setModificationDate(new Date());
        User user = repository.save(UserHelper.toEntity(userDTO));
        return UserHelper.fromEntity(user);
    }

    @Override
    public UserDTO save(UserDTO userDTO) throws SecurityException {
        if (repository.getUserByUsername(userDTO.getUsername()) != null) {
            throw new SecurityException(Messages.get("usernameAlreadyExists"));
        }
//        if (repository.getUserByPersonalId(userDTO.getPersonalId()) != null) {
//            throw new SecurityException(Messages.get("personalIdAlreadyExists"));
//        }
        userDTO.setCreationDate(new Date());
        userDTO.setActive(true);
        userDTO.setPassword(SecurityUtils.PASSWORD_ENCODER.encode(userDTO.getPassword()));
        userDTO.setChangePassword(true);
        User user = repository.save(UserHelper.toEntity(userDTO));
        return UserHelper.fromEntity(user);
    }

    @Override
    public User findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public UserDTO findByUsername(String username) {
        return UserHelper.fromEntity(repository.findUserByUsername(username));
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) throws SecurityException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = repository.getUserByUsername(authentication.getName());
        if (!SecurityUtils.PASSWORD_ENCODER.matches(oldPassword, user.getPassword())) {
            throw new SecurityException(Messages.get("oldPasswordDidNotMatch"));
        }
        user.setPassword(SecurityUtils.PASSWORD_ENCODER.encode(newPassword));
        user.setChangePassword(false);
        repository.save(user);
    }

    @Override
    public UserDTO getUserById(Long id) throws SecurityException {
        User found = repository.findOne(id);
        if (found == null) {
            throw new SecurityException(Messages.get("userDoesNotExist"));
        }
        return UserHelper.fromEntity(found);
    }

    @Override
    public List<ComboObject> getUserTypes() {
        List<ComboObject> result = new ArrayList<>();
        for (UserType userType : UserType.values()) {
            result.add(new ComboObject(userType.name(), Messages.get(UserType.class.getSimpleName() + "_" + userType.name())));
        }
        return result;
    }

    @Override
    public User getUserByUsername(String name) {
        return repository.findUserByUsername(name);
    }

    @Override
    public UserDTO activate(long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("System user [" + authentication.getName() + "] activated user [" + id + "]");
        User curr = repository.getOne(id);
        curr.setActive(true);
        curr.setModificationDate(new Date());
        User saved = repository.save(curr);
        return UserHelper.fromEntity(saved);
    }

    @Override
    public UserDTO deactivate(long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("System user [" + authentication.getName() + "] deactivated user [" + id + "]");
        User curr = repository.getOne(id);
        curr.setActive(false);
        curr.setModificationDate(new Date());
        User saved = repository.save(curr);
        return UserHelper.fromEntity(saved);
    }

    @Override
    public void delete(long id) {
        repository.delete(id);
    }

    @Override
    public void changeUserPassword(Long userId, String newPassword) throws SecurityException {
        UserDTO user = getUserById(userId);
        user.setPassword(SecurityUtils.PASSWORD_ENCODER.encode(newPassword));
        user.setChangePassword(true);
        repository.save(UserHelper.toEntity(user));
    }

    @Override
    public List<String> usersByRole(String role) {
        return userStorage.findUsernamesByRole(role);
    }

    @Override
    public ListResult<UserDTO> findUsersWithoutDepartment(String query, int limit, int offset) {
        ListResult<User> users = userStorage.findUsersWithoutDepartment(query, limit, offset);
        ListResult<UserDTO> result = users.copy(UserDTO.class);
        result.setResultList(UserHelper.fromEntities(users.getResultList()));
        return result;
    }

    @Override
    public User getActiveUserByUsername(String username) throws SecurityException {
        User user = repository.findUserByUsername(username);
        if (user == null) {
            throw new SecurityException(Messages.get("userDoesNotExist"));
        }
        if (!user.getActive()) {
            throw new SecurityException(Messages.get("userIsNotActive"));
        }
        return user;
    }

}
