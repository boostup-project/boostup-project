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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonDbService lessonDbService;
    private final FileHandler fileHandler;
    private final MemberDbService memberDbService;

    @Transactional
    public void createLesson(PostLesson postLesson, Long memberId, MultipartFile profileImage, List<MultipartFile> careerImage) throws Exception {
        Member findMember = memberDbService.ifExistsReturnMember(memberId);
        Lesson lesson = Lesson.builder()
                .name(findMember.getName())
                .title(postLesson.getTitle())
                .company(postLesson.getCompany())
                .career(postLesson.getCareer())
                .memberId(memberId)
                .cost(postLesson.getCost())
                .build();

        UploadFile uploadFile = fileHandler.uploadFile(profileImage);

        ProfileImage createProfileImage = ProfileImage.builder()
                .originFileName(profileImage.getOriginalFilename())
                .fileName(profileImage.getOriginalFilename())
                .filePath(uploadFile.getFilePath())
                .fileSize(profileImage.getSize())
                .build();

        lesson.addProfileImage(createProfileImage);
        List<Long> lessonLanguagesList = postLesson.getLanguages();
        lessonDbService.saveLanguageList(lessonLanguagesList,lesson);
        List<Long> lessonAddressList = postLesson.getAddress();
        lessonDbService.saveAddressList(lessonAddressList,lesson);

        lessonDbService.saveLesson(lesson);

        Lesson saveLesson = lessonDbService.returnSavedLesson(lesson);
        LessonInfo lessonInfo = LessonInfo.builder()
                .lessonId(saveLesson.getId())
                .introduction(postLesson.getIntroduction())
                .companies(postLesson.getDetailCompany())
                .favoriteLocation(postLesson.getDetailLocation())
                .personality(postLesson.getPersonality())
                .costs(postLesson.getDetailCost())
                .build();

        List<UploadFile> uploadFileList = fileHandler.parseUploadFileInfo(careerImage);
        lessonDbService.saveCareerImage(uploadFileList, lessonInfo);
        lessonDbService.saveLessonInfo(lessonInfo);

        Curriculum curriculum = Curriculum.builder()
                .lessonId(saveLesson.getId())
                .curriculum(postLesson.getCurriculum())
                .build();
        lessonDbService.saveCurriculum(curriculum);
    }
    public void updateLesson(Long lessonId, PostEditLesson postEditLesson, Long memberId, MultipartFile file) {
        Member findMember = memberDbService.ifExistsReturnMember(memberId);
        Lesson updateLesson = lessonDbService.ifExistsReturnLesson(lessonId);
    }
}
