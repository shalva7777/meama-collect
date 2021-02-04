package com.meama.security.role.service;

import com.meama.common.response.ListResult;
import com.meama.common.security.PrivilegeDTO;
import com.meama.common.security.RoleDTO;
import com.meama.security.messages.Messages;
import com.meama.security.role.storage.*;
import com.meama.security.role.storage.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final RoleStorage storage;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, PrivilegeRepository privilegeRepository, RoleStorage storage) {
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
        this.storage = storage;
    }

    @Override
    public ListResult<RoleDTO> find(String query, int limit, int offset) {
        ListResult<Role> roles = storage.find(query, limit, offset);
        ListResult<RoleDTO> result = roles.copy(RoleDTO.class);
        result.setResultList(RoleHelper.fromEntities(roles.getResultList()));
        return result;
    }

    @Override
    public ListResult<RoleDTO> find(Long id, String name, int limit, int offset) {
        ListResult<Role> roles = storage.findByFilters(id, name, limit, offset);
        ListResult<RoleDTO> result = roles.copy(RoleDTO.class);
        result.setResultList(RoleHelper.fromEntities(roles.getResultList()));
        return result;
    }

    @Override
    public RoleDTO update(RoleDTO roleDTO) throws SecurityException {
        Role prevRole = roleRepository.findOne(roleDTO.getId());
        if (prevRole == null) {
            throw new SecurityException(Messages.get("objectNotFound"));
        }
        if (!roleDTO.getName().equals(prevRole.getName()) && roleRepository.findByName(roleDTO.getName()) != null) {
            throw new SecurityException(Messages.get("roleWithThisNameAlreadyExists"));
        }
        return RoleHelper.fromEntity(roleRepository.save(RoleHelper.toEntity(roleDTO)));
    }

    @Override
    public RoleDTO save(RoleDTO roleDTO) throws SecurityException {
        Role role = roleRepository.findByName(roleDTO.getName());
        if (role != null) {
            throw new SecurityException(Messages.get("roleWithThisNameAlreadyExists"));
        }
        Role saved = roleRepository.save(RoleHelper.toEntity(roleDTO));
        return RoleHelper.fromEntity(saved);
    }

    @Override
    public void delete(long id) {
        roleRepository.delete(id);
    }

    @Override
    public Map<String, List<PrivilegeDTO>> getAllPrivileges() {
        List<PrivilegeDTO> privileges = PrivilegeHelper.fromEntities(privilegeRepository.findAll());
        Map<String, List<PrivilegeDTO>> result = new HashMap<>();
        for (PrivilegeDTO privilege : privileges) {
            if (result.containsKey(privilege.getGroupName())) {
                if (!result.get(privilege.getGroupName()).contains(privilege)) {
                    result.get(privilege.getGroupName()).add(privilege);
                }
            } else {
                List<PrivilegeDTO> privilegeList = new ArrayList<>();
                privilegeList.add(privilege);
                result.put(privilege.getGroupName(), privilegeList);
            }
        }
        return result;
    }
}
