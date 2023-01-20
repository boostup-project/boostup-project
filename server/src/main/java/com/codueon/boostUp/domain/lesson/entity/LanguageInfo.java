package com.codueon.boostUp.domain.lesson.entity;

import com.codueon.boostUp.global.exception.BusinessLogicException;
import com.codueon.boostUp.global.exception.ExceptionCode;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum LanguageInfo {
    JAVASCRIPT(1, "Javascript"),
    PYTHON(2, "Python"),
    GO(3, "Go"),
    JAVA(4, "Java"),
    KOTLIN(5, "Kotlin"),
    PHP(6, "PHP"),
    C_SHOP(7, "C#"),
    SWIFT(8, "Swift");

    private final Integer status;
    private final String languages;

    LanguageInfo(Integer status,
                 String languages) {
        this.status = status;
        this.languages = languages;
    }

    private static final Map<Integer, LanguageInfo> descriptions =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(LanguageInfo::getStatus, Function.identity())));

    public static LanguageInfo findById(Integer languageId) {
        return Optional.ofNullable(descriptions.get(languageId))
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.LANGUAGE_NOT_FOUND));
    }
}
