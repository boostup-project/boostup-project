package com.codueon.boostUp.domain.lesson.service;

import com.codueon.boostUp.domain.lesson.entity.*;
import com.codueon.boostUp.domain.lesson.repository.*;
import com.codueon.boostUp.global.exception.BusinessLogicException;
import com.codueon.boostUp.global.exception.ExceptionCode;
import com.codueon.boostUp.global.file.AwsS3Service;
import com.codueon.boostUp.global.file.UploadFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class LessonDbService {
    private final LessonRepository lessonRepository;
    private final LessonInfoRepository lessonInfoRepository;
    private final CurriculumRepository curriculumRepository;
    private final AwsS3Service awsS3Service;
    // test

    /*--------------------------------------- DB Create 메서드 --------------------------------------*/

    /**
     * 요약 정보 프로필 사진 저장 메서드(과외 요약 저장 프로세스 - 1)
     *
     * @param profileImage 프로필 사진 정보
     * @param lesson       과외 요약 정보
     * @author Quartz614
     */
    public void saveProfileImage(List<MultipartFile> profileImage, Lesson lesson) {
        lesson.addProfileImage((ProfileImage) profileImage);
    }

    public void saveLesson(Lesson lesson) {
        lessonRepository.save(lesson);
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
     * 과외 요약 정보 조회 메서드(사용자 식별자)
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
    public LessonInfo ifExsitsReturnLessonInfo(Long lessonId) {
        return lessonInfoRepository.findByLessonId(lessonId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.LESSON_INFO_NOT_FOUND));
    }

    /**
     * 과외 커리큘럼 정보 조회 메서드
     *
     * @param lessonId 과외 식별자
     * @return Curriculum
     * @author mozzi327
     */
    public Curriculum ifExsistsReturnCurriculum(Long lessonId) {
        return curriculumRepository.findByLessonId(lessonId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CURRICULUM_NOT_FOUND));
    }

    /*--------------------------------------- DB Update 메서드 --------------------------------------*/

    /**
     * 언어 목록 생성 메서드
     * @param languageList 언어 목록
     * @return Language 언어
     * @author Quartz614
     */
    public List<LessonLanguage> makeLanguageList(List<Integer> languageList) {
        return languageList.stream().map(
                s -> {
                    return LessonLanguage.builder()
                            .languageId(s)
                            .build();

                }
        ).collect(Collectors.toList());
    }

    /**
     * 주소 목록 생성 메서드
     * @param addressList 주소 목록
     * @return Address 주소
     * @author Quartz614
     */
    public List<LessonAddress> makeAddressList(List<Integer> addressList) {
        return addressList.stream().map(
                s -> {
                    return LessonAddress.builder()
                            .addressId(s)
                            .build();

                }
        ).collect(Collectors.toList());
    }

    /**
     * 경력 이미지 수정 메서드
     * @param careerImages 경력 이미지
     * @param lessonInfo 과외 상세 정보
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
     * @param curriculum 커리큘럼
     * @author Quartz614
     */
    public void editCurriculum(Curriculum curriculum) {
        curriculumRepository.save(curriculum);
    }

    /*--------------------------------------- DB Delete 메서드 --------------------------------------*/

    /**
     * 과외 삭제 메서드
     * @param lesson 과외
     * @author Quartz614
     */
    public void deleteLesson(Lesson lesson) {
        lessonRepository.delete(lesson);
    }

    /**
     * 과외 상세 정보 삭제 메서드
     * @param lessonInfo 과외 상세 정보
     * @author Quartz614
     */
    public void deleteLessonInfo(LessonInfo lessonInfo) {
        lessonInfoRepository.delete(lessonInfo);
    }

    /**
     * 커리큘럼 삭제 메서드
     * @param curriculum 커리큘럼
     * @author Quartz614
     */
    public void deleteCurriculum(Curriculum curriculum) {
        curriculumRepository.delete(curriculum);
    }
}
