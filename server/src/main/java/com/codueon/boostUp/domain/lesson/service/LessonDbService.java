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

    public Lesson ifExistsReturnLesson(Long lessonId) {
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.LESSON_NOT_FOUND));
    }

    public void saveLesson(Lesson lesson) {
        lessonRepository.save(lesson);
    }

    public void saveLessonInfo(LessonInfo lessonInfo) {
        lessonInfoRepository.save(lessonInfo);
    }

    public void saveProfileImage(ProfileImage profileImage, Lesson lesson) {
        lesson.addProfileImage(profileImage);
    }

    public void saveCareerImage(List<UploadFile> uploadFileList, LessonInfo lessonInfo) {
        uploadFileList.forEach(uploadFiles-> {
            CareerImage createCareerImage = CareerImage.builder()
                    .originFileName(uploadFiles.getOriginFileName())
                    .fileName(uploadFiles.getFileName())
                    .filePath(uploadFiles.getFilePath())
                    .fileSize(uploadFiles.getFileSize())
                    .build();
            lessonInfo.addCareerImage(createCareerImage);
        });
    }

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

    public void saveCurriculum(Curriculum curriculum) {
        curriculumRepository.save(curriculum);
    }

    public void deleteLesson(Lesson lesson) {
        lessonRepository.delete(lesson);
    }

    public Lesson returnSavedLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    public Language findIfExistLanguage(Long languageId) {
        return languageRepository.findById(languageId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.LANGUAGE_NOT_FOUND));
    }

    public Address findIfExistAddress(Long addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ADDRESS_NOT_FOUND));
    }
}
