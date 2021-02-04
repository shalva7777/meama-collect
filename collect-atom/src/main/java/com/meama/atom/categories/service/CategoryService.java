package com.meama.atom.categories.service;

import com.meama.atom.exception.AtomException;
import com.meama.common.atom.categories.CategoryDTO;
import com.meama.common.response.ListResult;

public interface CategoryService {

    ListResult<CategoryDTO> find(String query, int limit, int offset, String orderBy, boolean asc);

    CategoryDTO save(CategoryDTO category) throws AtomException;

    CategoryDTO update(CategoryDTO category) throws AtomException;

    void delete(long id) throws AtomException;

    CategoryDTO activate(long id);

    CategoryDTO deactivate(long id);
}
