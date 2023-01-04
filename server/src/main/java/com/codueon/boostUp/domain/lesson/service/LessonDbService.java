package com.codueon.boostUp.domain.lesson.service;

import com.codueon.boostUp.domain.lesson.entity.*;
import com.codueon.boostUp.domain.lesson.repository.*;
import com.codueon.boostUp.global.exception.BusinessLogicException;
import com.codueon.boostUp.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessonDbService {
     private final LessonRepository lessonRepository;
     private final LessonInfoRepository lessonInfoRepository;
     private final ProfileImageRepository profileImageRepository;
     private final CareerImageRepository careerImageRepository;
     private final LanguageRepository languageRepository;
     private final LessonLanguageRepository lessonLanguageRepository;
     private final LessonAddressRepository lessonAddressRepository;
     private final AddressRepository addressRepository;
     private final CurriculumRepository curriculumRepository;

    public Lesson ifExistsReturnLesson(Long lessonId) {
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.LESSON_NOT_FOUND));
    }

    public void saveLesson(Lesson lesson) {
        lessonRepository.save(lesson);
    }
    public void saveLessonInfo(LessonInfo lessonInfo) { lessonInfoRepository.save(lessonInfo);}
    public void saveProfileImage(ProfileImage profileImage) {
        profileImageRepository.save(profileImage);
    }
    public void saveCareerImage(CareerImage careerImage) {
        careerImageRepository.save(careerImage);
    }

    public void saveLanguageList(List<Long> languageList, Lesson lesson) {
        languageList.forEach(
                s -> {
                    Optional<Language> language = languageRepository.findById(s);
                    LessonLanguage lessonLanguage = new LessonLanguage(s, lesson, language.get());
                    lessonLanguageRepository.save(lessonLanguage);
                }
        );
    }
    public void saveAddressList(List<Long> addressList, Lesson lesson) {
        addressList.forEach(
                s -> {
                    Optional<Address> address = addressRepository.findById(s);
                    LessonAddress lessonAddress = new LessonAddress(s, lesson, address.get());
                    lessonAddressRepository.save(lessonAddress);
                }
        );
    }
    public void saveCurriculum(Curriculum curriculum) {
        curriculumRepository.save(curriculum);
    }
    public void deleteLesson(Lesson lesson) {
        lessonRepository.delete(lesson);
    }
}
