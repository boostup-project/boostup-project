package com.codueon.boostUp.domain.lesson.service;

import com.codueon.boostUp.domain.lesson.entity.*;
import com.codueon.boostUp.domain.lesson.repository.*;
import com.codueon.boostUp.global.exception.BusinessLogicException;
import com.codueon.boostUp.global.exception.ExceptionCode;
import com.codueon.boostUp.global.file.UploadFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.codueon.boostUp.domain.lesson.entity.QLessonInfo.lessonInfo;

@Service
@Transactional
@RequiredArgsConstructor
public class LessonDbService {
     private final LessonRepository lessonRepository;
     private final LessonInfoRepository lessonInfoRepository;
     private final LanguageRepository languageRepository;
     private final AddressRepository addressRepository;
     private final CurriculumRepository curriculumRepository;

    /*--------------------------------------- DB Create 메서드 --------------------------------------*/

    /**
     * 요약 정보 프로필 사진 저장 메서드(과외 요약 저장 프로세스 - 1)
     * @param profileImage 프로필 사진 정보
     * @param lesson 과외 요약 정보
     * @author Quartz614
     */
    public void saveProfileImage(ProfileImage profileImage, Lesson lesson) {
        lesson.addProfileImage(profileImage);
    }

    /**
     * 과외 요약 정보 사용 언어 저장 메서드(과외 요약 저장 프로세스 - 2)
     * @param languageList 사용 언어 정보 리스트
     * @param lesson 과외 요약 정보
     * @author Quartz614
     */
    public void addLanguageList(List<Long> languageList, Lesson lesson) {
        languageList.forEach(
                s -> {
                    Language language = findIfExistLanguage(s);
                    LessonLanguage lessonLanguage = LessonLanguage.builder()
                            .languages(language)
                            .build();
                    lesson.addLessonLanguage(lessonLanguage);
                }
        );
    }

    /**
     * 과외 요약 정보 가능 지역 저장 메서드(과외 요약 저장 프로세스 - 3)
     * @param addressList 가능 지역 정보 리스트
     * @param lesson 과외 요약 정보
     * @author Quartz614
     */
    public void addAddressList(List<Long> addressList, Lesson lesson) {
        addressList.forEach(
                s -> {
                    Address address = findIfExistAddress(s);
                    LessonAddress lessonAddress = LessonAddress.builder()
                            .address(address)
                            .build();
                    lesson.addLessonAddress(lessonAddress);
                }
        );
    }

    /**
     * 과외 요약 정보 저장 메서드(과외 요약 저장 프로세스 - 4)
     * @param lesson 과외 요약 정보
     * @return Lesson
     * @author Quartz614
     */
    public Lesson returnSavedLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    /**
     * 과외 상세 정보 경력 사진 저장 메서드(과외 상세 저장 프로세스 - 1)
     * @param careerImages 경력 사진 정보 리스트
     * @param lessonInfo 과외 상세 정보
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
     * @param lessonInfo 과외 상세 정보
     * @author Quartz614
     */
    public void saveLessonInfo(LessonInfo lessonInfo) {
        lessonInfoRepository.save(lessonInfo);
    }

    /**
     * 과외 커리큘럼 정보 저장 메서드
     * @param curriculum 커리큘럼 정보
     * @author Quartz614
     */
    public void saveCurriculum(Curriculum curriculum) {
        curriculumRepository.save(curriculum);
    }

    /*--------------------------------------- DB Read 메서드 --------------------------------------*/

    /**
     * 사용 언어 정보 조회 메서드
     * @param languageId 사용 언어 식별자
     * @return Language
     * @author Quartz614
     */
    public Language findIfExistLanguage(Long languageId) {
        return languageRepository.findById(languageId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.LANGUAGE_NOT_FOUND));
    }

    /**
     * 주소 정보 조회 메서드
     * @param addressId 주소 식별자
     * @return Address
     * @author Quartz614
     */
    public Address findIfExistAddress(Long addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ADDRESS_NOT_FOUND));
    }

    /**
     * 과외 요약 정보 조회 메서드
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
     * @param lessonId 과외 식별자
     * @return Curriculum
     * @author mozzi327
     */
    public Curriculum ifExsistsReturnCurriculum(Long lessonId) {
        return curriculumRepository.findByLessonId(lessonId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CURRICULUM_NOT_FOUND));
    }

    /*--------------------------------------- DB Update 메서드 --------------------------------------*/









    /*--------------------------------------- DB Delete 메서드 --------------------------------------*/

    public void deleteLesson(Lesson lesson) {
        lessonRepository.delete(lesson);
    }




}
