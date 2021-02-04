package com.meama.security.role.service;

import com.meama.common.response.ListResult;
import com.meama.common.security.PrivilegeDTO;
import com.meama.common.security.RoleDTO;

import java.util.List;
import java.util.Map;

public interface RoleService {

    ListResult<RoleDTO> find(String query, int limit, int offset);

    ListResult<RoleDTO> find(Long id, String name, int limit, int offset);

    RoleDTO update(RoleDTO roleDTO) throws SecurityException;

    RoleDTO save(RoleDTO roleDTO) throws SecurityException;

    void delete(long id);

    Map<String, List<PrivilegeDTO>> getAllPrivileges();
}
