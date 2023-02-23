package com.codueon.boostUp.domain.lesson.service;

import com.codueon.boostUp.domain.lesson.dto.get.GetLessonInfoForAlarm;
import com.codueon.boostUp.domain.lesson.entity.*;
import com.codueon.boostUp.domain.lesson.repository.CurriculumRepository;
import com.codueon.boostUp.domain.lesson.repository.LessonInfoRepository;
import com.codueon.boostUp.domain.lesson.repository.LessonRepository;
import com.codueon.boostUp.domain.member.exception.AuthException;
import com.codueon.boostUp.global.exception.BusinessLogicException;
import com.codueon.boostUp.global.exception.ExceptionCode;
import com.codueon.boostUp.global.file.UploadFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class LessonDbService {
    private final LessonRepository lessonRepository;
    private final LessonInfoRepository lessonInfoRepository;
    private final CurriculumRepository curriculumRepository;

    /*--------------------------------------- DB Create 메서드 --------------------------------------*/

    public Lesson saveLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    /**
     * 과외 요약 정보 사용 언어 저장 메서드(과외 요약 저장 프로세스 - 2)
     *
     * @param languageList 사용 언어 정보 리스트
     * @param lesson       과외 요약 정보
     * @author Quartz614
     */
    public void addLanguageList(List<Integer> languageList, Lesson lesson) {
        languageList.forEach(
                s -> {
                    LessonLanguage lessonLanguage = LessonLanguage.builder()
                            .languageId(s)
                            .build();
                    lesson.addLessonLanguage(lessonLanguage);
                }
        );
    }

    /**
     * 과외 요약 정보 가능 지역 저장 메서드(과외 요약 저장 프로세스 - 3)
     *
     * @param addressList 가능 지역 정보 리스트
     * @param lesson      과외 요약 정보
     * @author Quartz614
     */
    public void addAddressList(List<Integer> addressList, Lesson lesson) {
        addressList.forEach(
                s -> {
                    LessonAddress lessonAddress = LessonAddress.builder()
                            .addressId(s)
                            .build();
                    lesson.addLessonAddress(lessonAddress);
                }
        );
    }

    /**
     * 과외 요약 정보 저장 메서드(과외 요약 저장 프로세스 - 4)
     *
     * @param lesson 과외 요약 정보
     * @return Lesson
     * @author Quartz614
     */
    public Lesson returnSavedLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    /**
     * 과외 상세 정보 경력 사진 저장 메서드(과외 상세 저장 프로세스 - 1)
     *
     * @param careerImages 경력 사진 정보 리스트
     * @param lessonInfo   과외 상세 정보
     * @author Quartz614
     */
    public void saveCareerImage(List<UploadFile> careerImages, LessonInfo lessonInfo) {
        careerImages.forEach(careerImage -> {
            CareerImage createCareerImage = CareerImage.builder()
                    .originFileName(careerImage.getOriginFileName())
                    .fileName(careerImage.getFileName())
                    .filePath(careerImage.getFilePath())
                    .fileSize(careerImage.getFileSize())
                    .build();
            lessonInfo.addCareerImage(createCareerImage);
        });
    }

    /**
     * 과외 상세 정보 저장 메서드(과외 상세 저장 프로세스 - 2)
     *
     * @param lessonInfo 과외 상세 정보
     * @author Quartz614
     */
    public void saveLessonInfo(LessonInfo lessonInfo) {
        lessonInfoRepository.save(lessonInfo);
    }

    /**
     * 과외 커리큘럼 정보 저장 메서드
     *
     * @param curriculum 커리큘럼 정보
     * @author Quartz614
     */
    public void saveCurriculum(Curriculum curriculum) {
        curriculumRepository.save(curriculum);
    }

    /*--------------------------------------- DB Read 메서드 --------------------------------------*/

    /**
     * memberId로 lessonId 조회 메서드
     * @param memberId 회원 식별자
     * @author Quartz614
     */
    public boolean ifExistsByMemberId(Long memberId) {
        return lessonRepository.existsByMemberId(memberId);
    }

    /**
     * 과외 요약 정보 조회 메서드
     *
     * @param lessonId 과외 식별자
     * @return Lesson
     * @author Quartz614
     */
    public Lesson ifExistsReturnLesson(Long lessonId) {
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.LESSON_NOT_FOUND));
    }

    /**
     * 과외 요양 정보 조회 메서드(과외 식별자, 사용자 식별자)
     *
     * @param lessonId 과외 식별자
     * @param memberId 사용자 식별자
     * @return Lesson
     * @author mozzi327
     */
    public Lesson ifExistsReturnLessonByLessonIdAndMemberId(Long lessonId, Long memberId) {
        return lessonRepository.findByIdAndMemberId(lessonId, memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.NO_AUTHORIZATION_EDIT_LESSON));
    }

    /**
     * 과외 요약 정보 조회 메서드(사용자 식별자)
     *
     * @param memberId 사용자 식별자
     * @return Lesson
     * @author mozzi327
     */
    public Lesson ifExistsReturnLessonByMemberId(Long memberId) {
        return lessonRepository.findByMemberId(memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.LESSON_NOT_FOUND));
    }

    /**
     * 과외 상세 정보 조회 메서드
     *
     * @param lessonId 과외 식별자
     * @return LessonInfo
     * @author mozzi327
     */
    public LessonInfo ifExistsReturnLessonInfo(Long lessonId) {
        return lessonInfoRepository.findByLessonId(lessonId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.LESSON_INFO_NOT_FOUND));
    }

    /**
     * 과외 상세 정보 조회 메서드(과외 식별자, 사용자 식별자)
     *
     * @param lessonId 과외 식별자
     * @param memberId 사용자 식별자
     * @return LessonInfo
     * @author mozzi327
     */
    public LessonInfo ifExistsReturnLessonInfoByLessonIdAndMemberId(Long lessonId, Long memberId) {
        return lessonInfoRepository.findByLessonIdAndMemberId(lessonId, memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.NO_AUTHORIZATION_EDIT_LESSON_INFO));
    }

    /**
     * 과외 커리큘럼 정보 조회 메서드
     *
     * @param lessonId 과외 식별자
     * @return Curriculum
     * @author mozzi327
     */
    public Curriculum ifExistsReturnCurriculum(Long lessonId) {
        return curriculumRepository.findByLessonId(lessonId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CURRICULUM_NOT_FOUND));
    }

    /**
     * 과외 커리큘럼 정보 조회 메서드(과외 식별자, 사용자 식별자)
     *
     * @param lessonId 과외 식별자
     * @param memberId 사용자 식별자
     * @return Curriculum
     * @author mozzi327
     */
    public Curriculum ifExistsReturnCurriculumByLessonIdAndMemberId(Long lessonId, Long memberId) {
        return curriculumRepository.findByLessonIdAndMemberId(lessonId, memberId)
                .orElseThrow(() -> new AuthException(ExceptionCode.NO_AUTHORIZATION_EDIT_CURRICULUM));
    }

    /**
     * 과외 등록한 사용자 식별자 조회 메서드
     *
     * @param lessonId 과외 식별자
     * @return Long
     * @author LeeGoh
     */
    public Long getMemberIdByLessonId(Long lessonId) {
        return lessonRepository.getMemberIdByLessonId(lessonId);
    }

    /**
     * 과외 등록한 사용자 닉네임 조회 메서드
     *
     * @param lessonId 과외 식별자
     * @return String
     * @author LeeGoh
     */
    public String getNameByLessonId(Long lessonId) {
        return lessonRepository.getNameByLessonId(lessonId);
    }

    /**
     * 알람용 LessonInfo 조회 메서드
     *
     * @param lessonId 과외 식별자
     * @return GetLessonInfoForAlarm
     * @author mozzi327
     */
    public GetLessonInfoForAlarm getLessonInfoForAlarm(Long lessonId) {
        return lessonRepository.getLessonInfoForAlarm(lessonId);
    }

    /*--------------------------------------- DB Update 메서드 --------------------------------------*/

    /**
     * 경력 이미지 수정 메서드
     *
     * @param careerImages 경력 이미지
     * @param lessonInfo   과외 상세 정보
     * @author Quartz614
     */
    public void editCareerImage(List<UploadFile> careerImages, LessonInfo lessonInfo) {
        careerImages.forEach(careerImage -> {
            CareerImage createCareerImage = CareerImage.builder()
                    .originFileName(careerImage.getOriginFileName())
                    .fileName(careerImage.getFileName())
                    .filePath(careerImage.getFilePath())
                    .fileSize(careerImage.getFileSize())
                    .build();
            lessonInfo.addCareerImage(createCareerImage);
        });
    }

    /**
     * 커리큘럼 수정 메서드
     *
     * @param curriculum 커리큘럼
     * @author Quartz614
     */
    public void editCurriculum(Curriculum curriculum) {
        curriculumRepository.save(curriculum);
    }

    /*--------------------------------------- DB Delete 메서드 --------------------------------------*/

    /**
     * 과외 삭제 메서드
     *
     * @param lesson 과외
     * @author Quartz614
     */
    public void deleteLesson(Lesson lesson) {
        lessonRepository.delete(lesson);
    }

    /**
     * 과외 상세 정보 삭제 메서드
     *
     * @param lessonInfo 과외 상세 정보
     * @author Quartz614
     */
    public void deleteLessonInfo(LessonInfo lessonInfo) {
        lessonInfoRepository.delete(lessonInfo);
    }

    /**
     * 커리큘럼 삭제 메서드
     *
     * @param lessonId 과외 식별자
     * @author Quartz614
     */
    public void deleteCurriculum(Long lessonId) {
        curriculumRepository.deleteByLessonId(lessonId);
    }
}
