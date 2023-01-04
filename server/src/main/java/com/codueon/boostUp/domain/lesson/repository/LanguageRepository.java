package com.codueon.boostUp.domain.lesson.repository;

import com.codueon.boostUp.domain.lesson.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Long> {
}
