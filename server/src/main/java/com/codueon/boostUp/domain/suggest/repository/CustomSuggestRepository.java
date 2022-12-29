package com.codueon.boostUp.domain.suggest.repository;

import com.codueon.boostUp.domain.suggest.dto.GetStudentSuggest;
import com.codueon.boostUp.domain.suggest.dto.GetTeacherSuggest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomSuggestRepository {

    Page<GetTeacherSuggest> getTeacherSuggestsOnMyPage(Long lessonId, Long memberId, int tabId, Pageable pageable);

    Page<GetStudentSuggest> getStudentSuggestsOnMyPage(Long memberId, Pageable pageable);
}
