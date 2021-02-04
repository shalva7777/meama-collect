package com.meama.meamacollect.application.controller.atom;

import com.meama.atom.categories.service.CategoryService;
import com.meama.common.atom.categories.CategoryDTO;
import com.meama.common.response.ListResult;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/atom/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping(value = "/{limit}/{offset}", produces = MediaType.APPLICATION_JSON_VALUE)
//    @PermissionCheck(action = "product_type_view")
    public ListResult<CategoryDTO> find(@PathVariable int limit,
                                        @PathVariable int offset,
                                        @RequestParam(value = "query", required = false) String query,
                                        @RequestParam(value = "orderBy", required = false) String orderBy,
                                        @RequestParam(value = "asc", required = false) boolean asc) {
        return categoryService.find(query, limit, offset, orderBy, asc);
    }

    @PutMapping(value = "/{id}")
//    @PermissionCheck(action = "product_type_manage")
//    @AuditLog(action = "update", target = "category", eventName = "Updated category [0]", args = {"{0}"})
    public CategoryDTO update(@RequestBody @Validated CategoryDTO categoryDTO, @PathVariable Long id) throws Exception {
        return categoryService.update(categoryDTO);
    }

//    @PostMapping(value = "")
////    @PermissionCheck(action = "product_type_manage")
//    @AuditLog(action = "create", target = "category", eventName = "Created category [0]", args = {"{0}"})
//    public CategoryDTO save(@RequestBody @Validated CategoryDTO categoryDTO, @PathVariable Long id) throws Exception {
//        return categoryService.save(categoryDTO);
//    }

    @PostMapping(value = "")
//    @PermissionCheck(action = "product_type_manage")
//    @AuditLog(action = "create", target = "category", eventName = "Created category [0]", args = {"{0}"})
    public CategoryDTO save(@RequestBody CategoryDTO categoryDTO) throws Exception {
        return categoryService.save(categoryDTO);
    }

    @PutMapping(value = "/{id}/activate", produces = MediaType.APPLICATION_JSON_VALUE)
//    @PermissionCheck(action = "product_type_manage")
//    @AuditLog(action = "activation", target = "category", eventName = "Activated category with id [0]", args = {"0"})
    public CategoryDTO activate(@PathVariable long id) {
        return categoryService.activate(id);
    }

    @PutMapping(value = "/{id}/deactivate", produces = MediaType.APPLICATION_JSON_VALUE)
//    @PermissionCheck(action = "product_type_manage")
//    @AuditLog(action = "deactivation", target = "category", eventName = "Deactivated category with id [0]", args = {"0"})
    public CategoryDTO deactivate(@PathVariable long id) {
        return categoryService.deactivate(id);
    }

}
