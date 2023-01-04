package com.codueon.boostUp.domain.lesson.service;
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
    public void createLesson(PostLesson postLesson, Long memberId, MultipartFile file,List<MultipartFile> files) throws Exception {
        Member findMember = memberDbService.ifExistsReturnMember(memberId);
        Lesson lesson = Lesson.builder()
                .name(findMember.getName())
                .title(postLesson.getTitle())
                .company(postLesson.getCompany())
                .career(postLesson.getCareer())
                .memberId(memberId)
                .cost(postLesson.getCost())
                .build();

        UploadFile uploadFile = fileHandler.uploadFile(file);
        lessonDbService.saveLesson(lesson);
        List<Long> lessonLanguagesList = postLesson.getLanguages();
        lessonDbService.saveLanguageList(lessonLanguagesList,lesson);
        List<Long> lessonAddressList = postLesson.getAddress();
        lessonDbService.saveAddressList(lessonAddressList,lesson);

        ProfileImage profileImage = ProfileImage.builder()
                .originFileName(file.getOriginalFilename())
                .fileName(file.getOriginalFilename())
                .filePath(uploadFile.getFilePath())
                .fileSize(file.getSize())
                .build();
        profileImage.addLesson(lesson);
        lessonDbService.saveProfileImage(profileImage);

        LessonInfo lessonInfo = LessonInfo.builder()
                .introduction(postLesson.getIntroduction())
                .companies(postLesson.getDetailCompany())
                .favoriteLocation(postLesson.getDetailLocation())
                .personality(postLesson.getPersonality())
                .costs(postLesson.getDetailCost())
                .build();
        lessonDbService.saveLessonInfo(lessonInfo);

        List<UploadFile> uploadFileList = fileHandler.parseUploadFileInfo(files);
        List<CareerImage> careerImageList = new ArrayList<>();

         uploadFileList.forEach(uploadFiles-> {
            CareerImage careerImage = CareerImage.builder()
                    .originFileName(uploadFiles.getOriginFileName())
                    .fileName(uploadFiles.getFileName())
                    .filePath(uploadFiles.getFilePath())
                    .fileSize(uploadFiles.getFileSize())
                    .build();
            careerImageList.add(careerImage);
        });

        //파일이 존재할 때 처리
        if (!careerImageList.isEmpty()) {
            for (CareerImage careerImage : careerImageList) {
                //파일 DB 저장
                careerImage.addLessonInfo(lessonInfo);
                lessonDbService.saveCareerImage(careerImage);
            }
        }
        Curriculum curriculum = Curriculum.builder()
                .lessonId(lesson.getId())
                .curriculum(postLesson.getCurriculum())
                .build();
        lessonDbService.saveCurriculum(curriculum);
    }
}
