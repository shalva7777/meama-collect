package com.meama.atom.categories.service;

import com.meama.atom.exception.AtomException;
import com.meama.common.atom.categories.CategoryDTO;
import com.meama.common.response.ListResult;

import java.util.List;

public interface CategoryService {

    ListResult<CategoryDTO> find(String query, int limit, int offset, String orderBy, boolean asc);

    ListResult<CategoryDTO> find(Long id, Long equalId, String name, Boolean active, int limit, int offset, String orderBy, boolean asc);

    List<CategoryDTO> findCategoriesForNewCategory(Long id);

    CategoryDTO save(CategoryDTO category) throws AtomException;

    CategoryDTO update(CategoryDTO category) throws AtomException;

    void delete(long id) throws AtomException;

    CategoryDTO activate(long id);

    CategoryDTO deactivate(long id);
}
