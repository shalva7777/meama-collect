package com.meama.atom.categories.service;

import com.meama.atom.categories.storage.CategoryHelper;
import com.meama.atom.categories.storage.CategoryRepository;
import com.meama.atom.categories.storage.CategoryStorage;
import com.meama.atom.categories.storage.model.Category;
import com.meama.atom.exception.AtomException;
import com.meama.atom.language.service.LanguageService;
import com.meama.atom.messages.Messages;
import com.meama.common.atom.categories.CategoryDTO;
import com.meama.common.response.ListResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;
    private final CategoryStorage categoryStorage;
    private final LanguageService languageService;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryStorage categoryStorage, LanguageService languageService) {
        this.categoryRepository = categoryRepository;
        this.categoryStorage = categoryStorage;
        this.languageService = languageService;
    }

    @Override
    public ListResult<CategoryDTO> find(String query, int limit, int offset, String orderBy, boolean asc) {
        ListResult<Category> categoryListResult = categoryStorage.find(query, limit, offset, "parentCategoryId", asc);
        ListResult<CategoryDTO> result = categoryListResult.copy(CategoryDTO.class);
        result.setResultList(CategoryHelper.fromEntities(categoryListResult.getResultList()));
        for (CategoryDTO categoryDTO : result.getResultList()) {
            if (categoryDTO.getParentCategoryId() != null) {
                getParentCategory(categoryDTO);
            }
        }
        return result;
    }

    @Override
    public ListResult<CategoryDTO> find(Long id, Long equalId, String name, Boolean active, int limit, int offset, String orderBy, boolean asc) {
        ListResult<Category> categoryListResult = categoryStorage.find(id, equalId, name, active, limit, offset, orderBy, asc);
        ListResult<CategoryDTO> result = categoryListResult.copy(CategoryDTO.class);
        result.setResultList(CategoryHelper.fromEntities(categoryListResult.getResultList()));
        for (CategoryDTO categoryDTO : result.getResultList()) {
            if (categoryDTO.getParentCategory() != null) {
                getParentCategory(categoryDTO);
            }
        }
        return result;
    }

    @Override
    public List<CategoryDTO> findCategoriesForNewCategory(Long id) {
        if (categoryStorage.countById(id) > 0) {
            return new ArrayList<>();
        }
        return CategoryHelper.fromEntities(categoryStorage.findCategoriesForNewCategory(id));
    }

    @Override
    public CategoryDTO save(CategoryDTO category) throws AtomException {
        Category byName = categoryRepository.findByName(category.getName());
        if (byName != null) {
            throw new AtomException(Messages.get("categorywithThisNameAlreadyExists"));
        }
        Category save = categoryRepository.save(CategoryHelper.toEntity(category));
        return getParentCategory(CategoryHelper.fromEntity(save));
    }

    @Override
    public CategoryDTO update(CategoryDTO category) throws AtomException {
        Category prevCategory = categoryRepository.findOne(category.getId());
        if (prevCategory == null) {
            throw new AtomException(Messages.get("objectNotFound"));
        }
        if (!prevCategory.getName().equals(category.getName()) && categoryRepository.findByName(category.getName()) != null) {
            throw new AtomException(Messages.get("categorywithThisNameAlreadyExists"));
        }
        Category save = categoryRepository.save(CategoryHelper.toEntity(category));
        return getParentCategory(CategoryHelper.fromEntity(save));
    }

    @Override
    public void delete(long id) throws AtomException {

    }

    @Override
    public CategoryDTO activate(long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("System user [" + authentication.getName() + "] activated product type [" + id + "]");
        Category curr = categoryRepository.getOne(id);
        curr.setActive(true);
        Category save = categoryRepository.save(curr);
        return getParentCategory(CategoryHelper.fromEntity(save));
    }

    @Override
    public CategoryDTO deactivate(long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("System user [" + authentication.getName() + "] deactivated product type [" + id + "]");
        Category curr = categoryRepository.getOne(id);
        curr.setActive(false);
        Category save = categoryRepository.save(curr);
        return getParentCategory(CategoryHelper.fromEntity(save));
    }

    private CategoryDTO getParentCategory(CategoryDTO category) {
        if (category.getParentCategoryId() != null) {
            category.setParentCategory(CategoryHelper.fromEntity(categoryRepository.getOne(category.getParentCategoryId())));
        }
        return category;
    }
}
