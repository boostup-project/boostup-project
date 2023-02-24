package com.codueon.boostUp.domain.bookmark.repository;

import com.codueon.boostUp.domain.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long>, CustomBookmarkRepository {
    Optional<Bookmark> findByMemberIdAndLessonId(Long memberId, Long lessonId);
    void deleteByLessonId(Long lessonId);
}
