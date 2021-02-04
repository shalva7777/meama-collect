package com.meama.atom.language.service;

import com.meama.atom.exception.AtomException;
import com.meama.common.atom.languages.LanguageDTO;
import com.meama.common.response.ListResult;

public interface LanguageService {

    ListResult<LanguageDTO> find(String query, int limit, int offset, String orderBy, boolean asc);

    LanguageDTO save(LanguageDTO language) throws AtomException;

    LanguageDTO update(LanguageDTO language) throws AtomException;

    void delete(long id) throws AtomException;

    LanguageDTO findMainLanguage();

    LanguageDTO markAsMain(long id);

    LanguageDTO unmarkMain(long id) throws AtomException;
}
