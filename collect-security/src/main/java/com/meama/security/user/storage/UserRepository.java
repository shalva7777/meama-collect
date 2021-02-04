package com.meama.security.user.storage;

import com.meama.security.user.storage.model.User;
import com.meama.security.user.storage.model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User getUserByUsername(String username);

    User findUserByUsername(String username);

    User getUserByIdAndUserType(Long id, UserType userType);

    Long countAllByActive(Boolean active);

    User getUserByPersonalId(String personalId);

    User findByEmail(String email);

}
