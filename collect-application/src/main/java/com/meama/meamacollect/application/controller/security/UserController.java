package com.meama.meamacollect.application.controller.security;

import com.meama.common.response.ComboObject;
import com.meama.common.response.ListResult;
import com.meama.common.security.ChangePasswordRequest;
import com.meama.common.security.LightWeightUser;
import com.meama.common.security.UserDTO;
import com.meama.common.security.UserTypeDTO;
import com.meama.security.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "api/auth/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/{limit}/{offset}")
//    @PermissionCheck(action = "user_view")
    public ListResult<UserDTO> find(@PathVariable int limit,
                                    @PathVariable int offset,
                                    @RequestParam(value = "query", required = false) String query,
                                    @RequestParam(value = "orderBy", required = false) String orderBy,
                                    @RequestParam(value = "asc", required = false) boolean asc
    ) {
        return userService.find(query, limit, offset, orderBy, asc);
    }

    @GetMapping(value = "/without-departments/{limit}/{offset}", produces = MediaType.APPLICATION_JSON_VALUE)
//    @PermissionCheck(action = "user_view")
    public ListResult<UserDTO> findUsersWithoutDepartment(@PathVariable int limit,
                                                          @PathVariable int offset,
                                                          @RequestParam(value = "query", required = false) String query) {
        return userService.findUsersWithoutDepartment(query, limit, offset);
    }

    @GetMapping(value = "/advance/{limit}/{offset}")
//    @PermissionCheck(action = "user_view")
    public ListResult<UserDTO> find(@RequestParam(value = "id", required = false) Long id,
                                    @RequestParam(value = "username", required = false) String username,
                                    @RequestParam(value = "firstName", required = false) String firstName,
                                    @RequestParam(value = "lastName", required = false) String lastName,
                                    @RequestParam(value = "sapCode", required = false) String sapCode,
                                    @RequestParam(value = "email", required = false) String email,
                                    @RequestParam(value = "phone", required = false) String phone,
                                    @RequestParam(value = "personalId", required = false) String personalId,
                                    @RequestParam(value = "fromModificationDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date fromModificationDate,
                                    @RequestParam(value = "toModificationDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date toModificationDate,
                                    @RequestParam(value = "fromCreationDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date fromCreationDate,
                                    @RequestParam(value = "toCreationDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date toCreationDate,
                                    @RequestParam(value = "active", required = false) Boolean active,
                                    @RequestParam(value = "roleId", required = false) Long roleId,
                                    @RequestParam(value = "type", required = false) UserTypeDTO type,
                                    @RequestParam(value = "companyId", required = false) Long companyId,
                                    @RequestParam(value = "fromRating", required = false) Integer fromRating,
                                    @RequestParam(value = "toRating", required = false) Integer toRating,
                                    @RequestParam(value = "orderBy", required = false) String orderBy,
                                    @RequestParam(value = "asc", required = false) boolean asc,
                                    @PathVariable int limit,
                                    @PathVariable int offset
    ) {
        return userService.find(id, username, firstName, lastName, sapCode, email, phone,
                personalId, active, fromCreationDate, toCreationDate, fromModificationDate,
                toModificationDate, roleId, type, companyId, fromRating, toRating, limit, offset, orderBy, asc);
    }

    @GetMapping(value = "/types", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ComboObject> getUserTypes() {
        return userService.getUserTypes();
    }

    @PutMapping(value = "/{id}")
//    @PermissionCheck(action = "user_manage")
//    @AuditLog(action = "update", target = "user", eventName = "Updated user [0]", args = {"{0}"})
    public UserDTO update(@RequestBody @Validated UserDTO user) throws Exception {
        return userService.update(user);
    }

    @PostMapping(value = "")
//    @PermissionCheck(action = "user_manage")
//    @AuditLog(action = "create", target = "user", eventName = "Created ciuserty [0]", args = {"{0}"})
    public UserDTO create(@RequestBody @Validated UserDTO user) throws Exception {
        return userService.save(user);
    }

    @PutMapping(value = "/self/password")
    public void changePassword(@RequestBody @Validated ChangePasswordRequest changePasswordRequest) throws Exception {
        userService.changePassword(changePasswordRequest.getOldPassword(), changePasswordRequest.getNewPassword());
    }

    @PutMapping(value = "/{id}/password")
//    @PermissionCheck(action = "user_manage")
    public void changeUserPassword(@RequestBody @Validated ChangePasswordRequest changePasswordRequest, @PathVariable Long id) throws Exception {
        userService.changeUserPassword(id, changePasswordRequest.getNewPassword());
    }

    @PutMapping(value = "/profile")
//    @AuditLog(action = "update", target = "user profile", eventName = "Updated user profile [0]", args = {"{0}"})
    public UserDTO editProfile(@RequestBody @Validated UserDTO user) throws Exception {
        return userService.update(user);
    }

    @PutMapping(value = "/{id}/activate")
//    @PermissionCheck(action = "user_manage")
//    @AuditLog(action = "activation", target = "user", eventName = "Activated user with id [0]", args = {"0"})
    public UserDTO activate(@PathVariable long id) {
        return userService.activate(id);
    }

    @PutMapping(value = "/{id}/deactivate")
//    @PermissionCheck(action = "user_manage")
//    @AuditLog(action = "deactivation", target = "user", eventName = "Deactivated user with id [0]", args = {"0"})
    public UserDTO deactivate(@PathVariable long id) {
        return userService.deactivate(id);
    }
}
