package com.meama.atom.categories.storage;

import com.meama.atom.categories.storage.model.Category;
import com.meama.common.atom.categories.CategoryDTO;

import java.util.ArrayList;
import java.util.List;

public class CategoryHelper {

    public static Category toEntity(CategoryDTO category) {
        if (category == null) {
            return null;
        }
        Category result = new Category();
        result.setId(category.getId());
        result.setName(category.getName());
        result.setImageUrl(category.getImageUrl());
        result.setParentCategoryId(category.getParentCategoryId());
        result.setActive(category.isActive());
        if (category.getParentCategory() != null) {
            result.setParentCategoryId(category.getParentCategory().getId());
        }

        result.setTranslations(CategoryTranslationHelper.toEntities(category.getTranslations()));
        return result;
    }

    public static CategoryDTO fromEntity(Category category) {
        if (category == null) {
            return null;
        }
        CategoryDTO result = new CategoryDTO();
        result.setId(category.getId());
        result.setName(category.getName());
        result.setImageUrl(category.getImageUrl());
        result.setParentCategoryId(category.getParentCategoryId());
        result.setActive(category.isActive());
        result.setTranslations(CategoryTranslationHelper.fromEntities(category.getTranslations()));
        return result;
    }

    public static List<Category> toEntities(List<CategoryDTO> categories) {
        if (categories == null) {
            return null;
        }
        List<Category> result = new ArrayList<>();
        for (CategoryDTO category : categories) {
            result.add(toEntity(category));
        }
        return result;
    }

    public static List<CategoryDTO> fromEntities(List<Category> categories) {
        if (categories == null) {
            return null;
        }
        List<CategoryDTO> result = new ArrayList<>();
        for (Category category : categories) {
            result.add(fromEntity(category));
        }
        return result;
    }
}
