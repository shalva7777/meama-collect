package com.meama.meamacollect.application.controller.atom;

import com.meama.atom.exception.AtomException;
import com.meama.atom.language.service.LanguageService;
import com.meama.common.atom.languages.LanguageDTO;
import com.meama.common.response.ListResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/atom/languages")
public class LanguageController {

    private final LanguageService service;

    @Autowired
    public LanguageController(LanguageService service) {
        this.service = service;
    }

    @GetMapping(value = "/{limit}/{offset}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ListResult<LanguageDTO> find(@PathVariable int limit,
                                        @PathVariable int offset,
                                        @RequestParam(value = "query", required = false) String query,
                                        @RequestParam(value = "orderBy", required = false) String orderBy,
                                        @RequestParam(value = "asc", required = false) boolean asc) {
        return service.find(query, limit, offset, orderBy, asc);
    }

    @GetMapping(value = "/main", produces = MediaType.APPLICATION_JSON_VALUE)
    public LanguageDTO find() {
        return service.findMainLanguage();
    }

    @PutMapping(value = "/{id}")
//    @PermissionCheck(action = "language_manage")
//    @AuditLog(action = "update", target = "language", eventName = "Updated language [0]", args = {"{0}"})
    public LanguageDTO update(@RequestBody @Validated LanguageDTO language, @PathVariable long id) throws Exception {
        return service.update(language);
    }

    @PostMapping(value = "")
//    @PermissionCheck(action = "language_manage")
//    @AuditLog(action = "create", target = "language", eventName = "Created language [0]", args = {"{0}"})
    public LanguageDTO save(@RequestBody @Validated LanguageDTO language) throws Exception {
        return service.save(language);
    }

    @DeleteMapping(value = "/{id}")
//    @PermissionCheck(action = "language_manage")
//    @AuditLog(action = "delete", target = "language", eventName = "Removed language with id [0]", args = {"0"})
    public void delete(@PathVariable long id) throws AtomException {
        service.delete(id);
    }

    @PutMapping(value = "/{id}/mark-as-main", produces = MediaType.APPLICATION_JSON_VALUE)
//    @PermissionCheck(action = "language_manage")
//    @AuditLog(action = "mark as main", target = "language", eventName = "Marked as main language with id [0]", args = {"0"})
    public LanguageDTO markAsMain(@PathVariable long id) {
        return service.markAsMain(id);
    }

    @PutMapping(value = "/{id}/unmark-main", produces = MediaType.APPLICATION_JSON_VALUE)
//    @PermissionCheck(action = "language_manage")
//    @AuditLog(action = "mark as not main", target = "language", eventName = "Marked as not main language with id [0]", args = {"0"})
    public LanguageDTO unmarkFromMain(@PathVariable long id) throws AtomException {
        return service.unmarkMain(id);
    }
}
