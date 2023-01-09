package com.codueon.boostUp.domain.bookmark.repository;

import com.codueon.boostUp.domain.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Book;
import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long>, CustomBookmarkRepository {
    Optional<Bookmark> findByMemberIdAndLessonId(Long memberId, Long lessonId);
    List<Bookmark> findAllByMemberId(Long memberId);
    List<Bookmark> removeAllByLessonId(Long lessonId);
    void deleteByLessonId(Long lessonId);
}
