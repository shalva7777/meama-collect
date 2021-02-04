package com.meama.meamacollect.application.controller.security;

import com.meama.common.response.ListResult;
import com.meama.common.security.PrivilegeDTO;
import com.meama.common.security.RoleDTO;
import com.meama.security.role.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/auth/roles")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping(value = "/{limit}/{offset}")
//    @PermissionCheck(action = "role_view")
    public ListResult<RoleDTO> find(@PathVariable int limit,
                                    @PathVariable int offset,
                                    @RequestParam(value = "query", required = false) String query) {
        return roleService.find(query, limit, offset);
    }

    @GetMapping(value = "/advance/{limit}/{offset}")
//    @PermissionCheck(action = "role_view")
    public ListResult<RoleDTO> find(@RequestParam(value = "id", required = false) Long id,
                                    @RequestParam(value = "name", required = false) String name,
                                    @PathVariable int limit,
                                    @PathVariable int offset) {
        return roleService.find(id, name, limit, offset);
    }

    @PutMapping(value = "/{id}")
//    @PermissionCheck(action = "role_manage")
//    @AuditLog(action = "update", target = "role", eventName = "Updated role [0]", args = {"{0}"})
    public RoleDTO update(@RequestBody @Validated RoleDTO roleDTO, @PathVariable String id) throws Exception {
        return roleService.update(roleDTO);
    }

    @PostMapping(value = "")
//    @PermissionCheck(action = "role_manage")
//    @AuditLog(action = "create", target = "role", eventName = "Created role [0]", args = {"{0}"})
    public RoleDTO save(@RequestBody @Validated RoleDTO roleDTO) throws Exception {
        return roleService.save(roleDTO);
    }

    @DeleteMapping(value = "/{id}")
//    @PermissionCheck(action = "role_manage")
//    @AuditLog(action = "delete", target = "role", eventName = "Deleted role with id [0]", args = {"0"})
    public void delete(@PathVariable Long id) {
        roleService.delete(id);
    }

    @GetMapping(value = "/privileges")
//    @PermissionCheck(action = "role_view")
    public Map<String, List<PrivilegeDTO>> findPrivileges() {
        return roleService.getAllPrivileges();
    }
}
