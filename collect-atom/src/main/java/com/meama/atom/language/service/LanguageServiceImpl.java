package com.meama.atom.language.service;

import com.meama.atom.exception.AtomException;
import com.meama.atom.language.storage.LanguageHelper;
import com.meama.atom.language.storage.LanguageRepository;
import com.meama.atom.language.storage.LanguageStorage;
import com.meama.atom.language.storage.model.Language;
import com.meama.atom.messages.Messages;
import com.meama.common.atom.languages.LanguageDTO;
import com.meama.common.response.ListResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class LanguageServiceImpl implements LanguageService {

    private static final Logger log = LoggerFactory.getLogger(LanguageServiceImpl.class);

    private final LanguageStorage storage;
    private final LanguageRepository repository;

    @Autowired
    public LanguageServiceImpl(LanguageStorage storage, LanguageRepository repository) {
        this.storage = storage;
        this.repository = repository;
    }

    @Override
    public ListResult<LanguageDTO> find(String query, int limit, int offset, String orderBy, boolean asc) {
        ListResult<Language> languages = storage.find(query, limit, offset, orderBy, asc);
        ListResult<LanguageDTO> result = languages.copy(LanguageDTO.class);
        result.setResultList(LanguageHelper.fromEntities(languages.getResultList()));
        return result;
    }

    @Override
    public LanguageDTO save(LanguageDTO language) throws AtomException {
        Language prev = repository.findByCode(language.getCode());
        if (prev != null) {
            throw new AtomException(Messages.get("RecordByThisCodeExists"));
        }
        // Mark previous main language as false
        if (language.getMain()) {
            Language previousMainLanguage = repository.findByMainIsTrue();
            if (previousMainLanguage != null) {
                previousMainLanguage.setMain(false);
                repository.save(previousMainLanguage);
            }
        }
        return LanguageHelper.fromEntity(repository.save(LanguageHelper.toEntity(language)));
    }

    @Override
    public LanguageDTO update(LanguageDTO language) throws AtomException {
        Language prev = repository.findOne(language.getId());
        if (prev == null) {
            throw new AtomException(Messages.get("objectNotFound"));
        }
        if (!prev.getCode().equals(language.getCode()) && repository.findByCode(language.getCode()) != null) {
            throw new AtomException(Messages.get("RecordByThisCodeExists"));
        }
        if (!prev.getMain() && language.getMain()) {
            Language previousMainLanguage = repository.findByMainIsTrue();
            if (previousMainLanguage != null) {
                previousMainLanguage.setMain(false);
                repository.save(previousMainLanguage);
            }
        }
        if (prev.getMain() && !language.getMain()) {
            Language otherMainLanguage = repository.findByMainIsTrueAndIdIsNot(prev.getId());
            if (otherMainLanguage == null) {
                throw new AtomException(Messages.get("markAnotherLanguageAsMain"));
            }
        }
        return LanguageHelper.fromEntity(repository.save(LanguageHelper.toEntity(language)));
    }

    @Override
    public void delete(long id) throws AtomException {
        Language found = repository.getOne(id);
        if (found.getMain()) {
            throw new AtomException(Messages.get("mainLanguageCannotBeDeleted"));
        }
        repository.delete(id);
    }

    @Override
    public LanguageDTO findMainLanguage() {
        return LanguageHelper.fromEntity(repository.findByMainIsTrue());

    }

    @Override
    public LanguageDTO markAsMain(long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Language prev = repository.findOne(id);
        log.info("System user [" + authentication.getName() + "] marked as main language [" + prev.getCode() + "]");
        if (!prev.getMain()) {
            Language previousMainLanguage = repository.findByMainIsTrue();
            if (previousMainLanguage != null) {
                previousMainLanguage.setMain(false);
                repository.save(previousMainLanguage);
            }
        }
        prev.setMain(true);
        return LanguageHelper.fromEntity(repository.save(prev));
    }

    @Override
    public LanguageDTO unmarkMain(long id) throws AtomException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Language prev = repository.findOne(id);
        log.info("System user [" + authentication.getName() + "] unmarked main from language [" + prev.getCode() + "]");
        if (prev.getMain()) {
            Language otherMainLanguage = repository.findByMainIsTrueAndIdIsNot(prev.getId());
            if (otherMainLanguage == null) {
                throw new AtomException(Messages.get("markAnotherLanguageAsMain"));
            }
        }
        prev.setMain(false);
        return LanguageHelper.fromEntity(repository.save(prev));
    }
}
