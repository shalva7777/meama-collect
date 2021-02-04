package com.meama.atom.language.storage;

import com.meama.atom.language.storage.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Long> {

    Language findByCode(String code);

    Language findByMainIsTrueAndIdIsNot(Long id);

    Language findByMainIsTrue();
}

