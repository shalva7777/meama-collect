package com.meama.atom.categories.storage;

import com.meama.atom.categories.storage.model.CategoryTranslation;
import com.meama.atom.language.storage.LanguageHelper;
import com.meama.common.common.StandardTranslationDTO;

import java.util.ArrayList;
import java.util.List;

public class CategoryTranslationHelper {

    public static CategoryTranslation toEntity(StandardTranslationDTO translation) {
        if (translation == null) {
            return null;
        }
        CategoryTranslation result = new CategoryTranslation();
        result.setId(translation.getId());
        result.setLanguage(LanguageHelper.toEntity(translation.getLanguage()));
        result.setName(translation.getName());
        return result;
    }

    public static StandardTranslationDTO fromEntity(CategoryTranslation translation) {
        if (translation == null) {
            return null;
        }
        StandardTranslationDTO result = new StandardTranslationDTO();
        result.setId(translation.getId());
        result.setLanguage(LanguageHelper.fromEntity(translation.getLanguage()));
        result.setName(translation.getName());
        return result;
    }

    public static List<CategoryTranslation> toEntities(List<StandardTranslationDTO> translations) {
        if (translations == null) {
            return null;
        }
        List<CategoryTranslation> result = new ArrayList<>();
        for (StandardTranslationDTO translation : translations) {
            result.add(toEntity(translation));
        }
        return result;
    }

    public static List<StandardTranslationDTO> fromEntities(List<CategoryTranslation> translations) {
        if (translations == null) {
            return null;
        }
        List<StandardTranslationDTO> result = new ArrayList<>();
        for (CategoryTranslation translation : translations) {
            result.add(fromEntity(translation));
        }
        return result;
    }

}
