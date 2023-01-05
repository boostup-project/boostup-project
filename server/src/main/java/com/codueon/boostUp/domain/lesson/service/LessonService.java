package com.codueon.boostUp.domain.lesson.service;

import com.codueon.boostUp.domain.lesson.dto.PostEditLesson;
import com.codueon.boostUp.domain.lesson.dto.PostLesson;
import com.codueon.boostUp.domain.lesson.entity.*;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.member.service.MemberDbService;
import com.codueon.boostUp.global.file.AwsS3Service;
import com.codueon.boostUp.global.file.FileHandler;
import com.codueon.boostUp.global.file.UploadFile;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonDbService lessonDbService;
    private final FileHandler fileHandler;
    private final MemberDbService memberDbService;

    /**
     * 과외 등록 메서드
     * @param postLesson 과외 등록 정보
     * @param memberId 사용자 식별자
     * @param profileImage 프로필 사진
     * @param careerImage 경력 사진
     * @author Quartz614
     */
    @Transactional
    public void createLesson(PostLesson postLesson,
                             Long memberId,
                             MultipartFile profileImage,
                             List<MultipartFile> careerImage) {
        Member findMember = memberDbService.ifExistsReturnMember(memberId);
        Lesson savedLesson = saveLessonAndReturnLesson(postLesson, findMember, profileImage);
        saveLessonInfo(savedLesson, postLesson, careerImage);
        saveCurriculum(savedLesson, postLesson);
    }

    /**
     * Lesson을 저장하고 객체를 리턴하는 메서드
     * @param postLesson 과외 등록 정보
     * @param member 사용자 식별자
     * @param profileImage 프로필 사진
     * @return Lesson
     * @author Quartz614
     */
    @SneakyThrows
    private Lesson saveLessonAndReturnLesson(PostLesson postLesson,
                                             Member member,
                                             MultipartFile profileImage) {
        Lesson lesson = Lesson.toEntity(postLesson, member.getName(), member.getId());

        UploadFile uploadFile = fileHandler.uploadFile(profileImage);
        ProfileImage createProfileImage = ProfileImage.toEntity(profileImage, uploadFile.getFilePath());

        createProfileImage.addLesson(lesson);
        lesson.addProfileImage(createProfileImage);

        lessonDbService.addLanguageList(postLesson.getLanguages(), lesson);
        lessonDbService.addAddressList(postLesson.getAddress(), lesson);

        return lessonDbService.returnSavedLesson(lesson);
    }

    /**
     * 과외 디테일 정보 저장 메서드
     * @param savedLesson 저장 후 조회된 요약 정보
     * @param postLesson 과외 등록 정보
     * @param careerImage 경력 사진
     * @author Quartz614
     */
    @SneakyThrows
    private void saveLessonInfo(Lesson savedLesson,
                                PostLesson postLesson,
                                List<MultipartFile> careerImage) {
        LessonInfo lessonInfo = LessonInfo.toEntity(savedLesson.getId(), postLesson);
        List<UploadFile> careerImages = fileHandler.parseUploadFileInfo(careerImage);
        lessonDbService.saveCareerImage(careerImages, lessonInfo);
        lessonDbService.saveLessonInfo(lessonInfo);
    }

    /**
     * 커리큘럼 저장 메서드
     * @param savedLesson 저장 후 조회된 요약 정보
     * @param postLesson 과외 등록 정보
     * @author Quartz614
     */
    private void saveCurriculum(Lesson savedLesson, PostLesson postLesson) {
        Curriculum curriculum = Curriculum.builder()
                .lessonId(savedLesson.getId())
                .curriculum(postLesson.getCurriculum())
                .build();

        lessonDbService.saveCurriculum(curriculum);
    }

    public void updateLesson(Long lessonId,
                             PostEditLesson postEditLesson,
                             Long memberId,
                             MultipartFile file) {
        Member findMember = memberDbService.ifExistsReturnMember(memberId);
        Lesson updateLesson = lessonDbService.ifExistsReturnLesson(lessonId);
    }
}
