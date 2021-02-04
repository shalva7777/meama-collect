package com.meama.security.role.storage;

import com.meama.security.role.storage.model.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeRepository extends JpaRepository<Privilege, String> {

}
