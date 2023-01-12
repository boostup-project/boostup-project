package com.codueon.boostUp.domain.lesson.service;

import com.codueon.boostUp.domain.bookmark.repository.BookmarkRepository;
import com.codueon.boostUp.domain.bookmark.service.BookmarkService;
import com.codueon.boostUp.domain.lesson.dto.PatchLessonCurriculum;
import com.codueon.boostUp.domain.lesson.dto.PostLesson;
import com.codueon.boostUp.domain.lesson.dto.PostLessonDetailEdit;
import com.codueon.boostUp.domain.lesson.dto.PostLessonInfoEdit;
import com.codueon.boostUp.domain.lesson.entity.*;
import com.codueon.boostUp.domain.lesson.repository.LessonInfoRepository;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.member.service.MemberDbService;
import com.codueon.boostUp.domain.reveiw.entity.Review;
import com.codueon.boostUp.domain.reveiw.service.ReviewService;
import com.codueon.boostUp.domain.suggest.dto.GetSuggestInfo;
import com.codueon.boostUp.domain.suggest.entity.Suggest;
import com.codueon.boostUp.domain.suggest.service.SuggestDbService;
import com.codueon.boostUp.global.exception.BusinessLogicException;
import com.codueon.boostUp.global.exception.ExceptionCode;
import com.codueon.boostUp.global.file.AwsS3Service;
import com.codueon.boostUp.global.file.FileHandler;
import com.codueon.boostUp.global.file.UploadFile;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.transaction.Transactional;
import java.util.List;



@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonDbService lessonDbService;
    private final FileHandler fileHandler;
    private final AwsS3Service awsS3Service;
    private final MemberDbService memberDbService;
    private final SuggestDbService suggestDbService;
    private final ReviewService reviewService;
    private final BookmarkRepository bookmarkRepository;
    private final LessonInfoRepository lessonInfoRepository;

    /**
     * 과외 등록 메서드 (Local)
     *
     * @param postLesson   과외 등록 정보
     * @param memberId     사용자 식별자
     * @param profileImage 프로필 사진
     * @param careerImage  경력 사진
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
     * 과외 등록 메서드 (S3)
     *
     * @param postLesson   과외 등록 정보
     * @param memberId     사용자 식별자
     * @param profileImage 프로필 사진
     * @param careerImage  경력 사진
     * @author Quartz614
     */
    @Transactional
    public void createLessonS3(PostLesson postLesson,
                             Long memberId,
                             MultipartFile profileImage,
                             List<MultipartFile> careerImage) {
        Member findMember = memberDbService.ifExistsReturnMember(memberId);
        Lesson savedLesson = saveLessonAndReturnLessonS3(postLesson, findMember, profileImage);
        saveLessonInfoS3(savedLesson, postLesson, careerImage);
        saveCurriculum(savedLesson, postLesson);
    }

    /**
     * Lesson을 저장하고 객체를 리턴하는 메서드 (Local)
     *
     * @param postLesson   과외 등록 정보
     * @param member       사용자 식별자
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
        ProfileImage createProfileImage = ProfileImage.toEntity(uploadFile, uploadFile.getFilePath());

        createProfileImage.addLesson(lesson);
        lesson.addProfileImage(createProfileImage);

        lessonDbService.addLanguageList(postLesson.getLanguages(), lesson);
        lessonDbService.addAddressList(postLesson.getAddress(), lesson);

        return lessonDbService.returnSavedLesson(lesson);
    }

    /**
     * Lesson을 저장하고 객체를 리턴하는 메서드 (S3)
     *
     * @param postLesson   과외 등록 정보
     * @param member       사용자 식별자
     * @param profileImage 프로필 사진
     * @return Lesson
     * @author Quartz614
     */
    @SneakyThrows
    private Lesson saveLessonAndReturnLessonS3(PostLesson postLesson,
                                             Member member,
                                             MultipartFile profileImage) {
        Lesson lesson = Lesson.toEntity(postLesson, member.getName(), member.getId());

        String dir = "profileImage";
        UploadFile uploadFile = awsS3Service.uploadfile(profileImage, dir);
        ProfileImage createProfileImage = ProfileImage.toEntity(uploadFile, uploadFile.getFilePath());

        createProfileImage.addLesson(lesson);
        lesson.addProfileImage(createProfileImage);

        lessonDbService.addLanguageList(postLesson.getLanguages(), lesson);
        lessonDbService.addAddressList(postLesson.getAddress(), lesson);

        return lessonDbService.returnSavedLesson(lesson);
    }

    /**
     * 과외 디테일 정보 저장 메서드 (Local)
     *
     * @param savedLesson 저장 후 조회된 요약 정보
     * @param postLesson  과외 등록 정보
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
     * 과외 디테일 정보 저장 메서드 (S3)
     *
     * @param savedLesson 저장 후 조회된 요약 정보
     * @param postLesson  과외 등록 정보
     * @param careerImage 경력 사진
     * @author Quartz614
     */
    @SneakyThrows
    private void saveLessonInfoS3(Lesson savedLesson,
                                PostLesson postLesson,
                                List<MultipartFile> careerImage) {
        LessonInfo lessonInfo = LessonInfo.toEntity(savedLesson.getId(), postLesson);
        String dir = "careerImage";
        List<UploadFile> careerImages = awsS3Service.uploadFileList(careerImage, dir);
        lessonDbService.saveCareerImage(careerImages, lessonInfo);
        lessonDbService.saveLessonInfo(lessonInfo);
    }

    /**
     * 커리큘럼 저장 메서드
     *
     * @param savedLesson 저장 후 조회된 요약 정보
     * @param postLesson  과외 등록 정보
     * @author Quartz614
     */
    private void saveCurriculum(Lesson savedLesson, PostLesson postLesson) {
        Curriculum curriculum = Curriculum.builder()
                .lessonId(savedLesson.getId())
                .curriculum(postLesson.getCurriculum())
                .build();

        lessonDbService.saveCurriculum(curriculum);
    }

    /**
     * 과외 요약 정보 수정 메서드 (로컬)
     *
     * @param lessonId 과외 식별자
     * @param postLessonInfoEdit 수정 과외 요약정보
     * @param memberId 회원 식별자
     * @param profileImage 회원 프로필 이미지
     * @author Quartz614
     */
    @SneakyThrows
    public void updateLessonInfo(Long lessonId,
                                 PostLessonInfoEdit postLessonInfoEdit,
                                 Long memberId,
                                 MultipartFile profileImage) {
        Member findMember = memberDbService.ifExistsReturnMember(memberId);
        Lesson updateLesson = lessonDbService.ifExistsReturnLesson(lessonId);
//        if (!Objects.equals(updateLesson.getMemberId(), findMember)) {
//            throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED_FOR_UPDATE);
//        }

        updateLesson.editLessonInfo(postLessonInfoEdit);

        List<Long> languageList = postLessonInfoEdit.getLanguages();

        List<Long> addressList = postLessonInfoEdit.getAddresses();

        lessonDbService.addLanguageList(languageList, updateLesson);
        lessonDbService.addAddressList(addressList, updateLesson);

        UploadFile uploadFile = fileHandler.uploadFile(profileImage);
        ProfileImage editProfileImage = ProfileImage.builder()
                .originFileName(uploadFile.getOriginFileName())
                .fileName(uploadFile.getFileName())
                .filePath(uploadFile.getFilePath())
                .fileSize(uploadFile.getFileSize())
                .build();
        updateLesson.addProfileImage(editProfileImage);
        lessonDbService.saveLesson(updateLesson);
    }

    /**
     * 과외 요약 정보 수정 메서드 (S3)
     *
     * @param lessonId 과외 식별자
     * @param postLessonInfoEdit 수정 과외 요약정보
     * @param memberId 회원 식별자
     * @param profileImage 회원 프로필 이미지
     * @author Quartz614
     */
    @SneakyThrows
    public void updateLessonInfoS3(Long lessonId,
                                 PostLessonInfoEdit postLessonInfoEdit,
                                 Long memberId,
                                 MultipartFile profileImage) {
        Member findMember = memberDbService.ifExistsReturnMember(memberId);
        Lesson updateLesson = lessonDbService.ifExistsReturnLesson(lessonId);
//        if (!Objects.equals(updateLesson.getMemberId(), findMember)) {
//            throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED_FOR_UPDATE);
//        }
        String dir = "profileImage";
        updateLesson.editLessonInfo(postLessonInfoEdit);
        ProfileImage profileImage1 = updateLesson.getProfileImage();

        awsS3Service.delete(profileImage1.getFileName(),dir);

        List<Long> languageList = postLessonInfoEdit.getLanguages();
        List<Long> addressList = postLessonInfoEdit.getAddresses();

        lessonDbService.addLanguageList(languageList, updateLesson);
        lessonDbService.addAddressList(addressList, updateLesson);

        UploadFile uploadFile = awsS3Service.uploadfile(profileImage, dir);
        ProfileImage editProfileImage = ProfileImage.builder()
                .originFileName(uploadFile.getOriginFileName())
                .fileName(uploadFile.getFileName())
                .filePath(uploadFile.getFilePath())
                .fileSize(uploadFile.getFileSize())
                .build();
        updateLesson.addProfileImage(editProfileImage);
        lessonDbService.saveLesson(updateLesson);
    }
    /**
     * 과외 상세 정보 수정 메서드 (로컬)
     * @param lessonId 과외 식별자
     * @param postLessonDetailEdit 수정 과외 상세정보
     * @param memberId 회원 식별자
     * @param careerImage 경력 이미지
     * @author Quartz614
     */
    @SneakyThrows
    public void updateLessonDetail(Long lessonId,
                                   PostLessonDetailEdit postLessonDetailEdit,
                                   Long memberId,
                                   List<MultipartFile> careerImage) {
        Member findMember = memberDbService.ifExistsReturnMember(memberId);
        LessonInfo updateLessonDetail = lessonDbService.ifExsitsReturnLessonInfo(lessonId);
        updateLessonDetail.editLessonDetail(postLessonDetailEdit);
        List<UploadFile> uploadFileList = fileHandler.parseUploadFileInfo(careerImage);
        lessonDbService.editCareerImage(uploadFileList, updateLessonDetail);
        lessonDbService.saveLessonInfo(updateLessonDetail);
    }

    /**
     * 과외 상세 정보 수정 메서드 (S3)
     * @param lessonId 과외 식별자
     * @param postLessonDetailEdit 수정 과외 상세정보
     * @param memberId 회원 식별자
     * @param careerImage 경력 이미지
     * @author Quartz614
     */
    @SneakyThrows
    public void updateLessonDetailS3(Long lessonId,
                                   PostLessonDetailEdit postLessonDetailEdit,
                                   Long memberId,
                                   List<MultipartFile> careerImage) {
        Member findMember = memberDbService.ifExistsReturnMember(memberId);
        LessonInfo updateLessonDetail = lessonDbService.ifExsitsReturnLessonInfo(lessonId);
        //        if (!Objects.equals(updateLessonDetail.getMemberId(), findMember)) {
//            throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED_FOR_UPDATE);
//        }
        String dir = "careerImage";
        updateLessonDetail.getCareerImages().forEach(careerImage1 -> awsS3Service.delete(careerImage1.getFileName(), dir));
        updateLessonDetail.editLessonDetail(postLessonDetailEdit);

        List<UploadFile> uploadFileList = awsS3Service.uploadFileList(careerImage, dir);
        lessonDbService.editCareerImage(uploadFileList, updateLessonDetail);
        lessonDbService.saveLessonInfo(updateLessonDetail);
    }

    /**
     * 커리큘럼 수정 메서드
     * @param lessonId 과외 식별자
     * @param patchLessonCurriculum 수정 커리큘럼 정보
     * @param memberId 회원 식별자
     * @author Quartz614
     */
    @SneakyThrows
    public void updateCurriculum(Long lessonId,
                                       PatchLessonCurriculum patchLessonCurriculum,
                                       Long memberId) {
        Member findMember = memberDbService.ifExistsReturnMember(memberId);
        Curriculum updateCurriculum = lessonDbService.ifExsistsReturnCurriculum(lessonId);
        updateCurriculum.editCurriculum(patchLessonCurriculum);
        lessonDbService.editCurriculum(updateCurriculum);
    }

    /**
     * 과외 삭제 메서드 (로컬)
     * @param memberId 회원 식별자
     * @param lessonId 과외 식별자
     * @author Quartz614
     */
    @Transactional
    public void deleteLesson(Long memberId, Long lessonId) {
        Member findMember = memberDbService.ifExistsReturnMember(memberId);
        Lesson findLesson = lessonDbService.ifExistsReturnLesson(lessonId);
        LessonInfo findLessonInfo = lessonDbService.ifExsitsReturnLessonInfo(lessonId);
        Curriculum findCurriculum = lessonDbService.ifExsistsReturnCurriculum(lessonId);
        List<Suggest> findSuggest = suggestDbService.findAllSuggestForLesson(lessonId);

        reviewService.removeAllByReviews(lessonId);
        bookmarkRepository.deleteByLessonId(lessonId);

        for (Suggest suggest : findSuggest) {
            if (suggest.getStatus().equals(Suggest.SuggestStatus.ACCEPT_IN_PROGRESS)) {
                throw new BusinessLogicException(ExceptionCode.NOT_ACCEPT_SUGGEST);
            } else if (suggest.getStatus().equals(Suggest.SuggestStatus.DURING_LESSON)) {
                throw new BusinessLogicException(ExceptionCode.NOT_PAY_SUCCESS);
            } else if (suggest.getStatus().equals(Suggest.SuggestStatus.PAY_IN_PROGRESS)) {
                throw new BusinessLogicException(ExceptionCode.NOT_PAY_SUCCESS);
            }
        }
        lessonDbService.deleteLesson(findLesson);
        lessonDbService.deleteLessonInfo(findLessonInfo);
        lessonDbService.deleteCurriculum(findCurriculum);
    }

    /**
     * 과외 삭제 메서드 (로컬)
     * @param memberId 회원 식별자
     * @param lessonId 과외 식별자
     * @author Quartz614
     */
    @Transactional
    public void deleteLessonS3(Long memberId, Long lessonId) {
        Member findMember = memberDbService.ifExistsReturnMember(memberId);
        Lesson findLesson = lessonDbService.ifExistsReturnLesson(lessonId);
        LessonInfo findLessonInfo = lessonDbService.ifExsitsReturnLessonInfo(lessonId);
        Curriculum findCurriculum = lessonDbService.ifExsistsReturnCurriculum(lessonId);
        List<Suggest> findSuggest = suggestDbService.findAllSuggestForLesson(lessonId);

        reviewService.removeAllByReviews(lessonId);
        bookmarkRepository.deleteByLessonId(lessonId);

        for (Suggest suggest : findSuggest) {
            if (suggest.getStatus().equals(Suggest.SuggestStatus.ACCEPT_IN_PROGRESS)) {
                throw new BusinessLogicException(ExceptionCode.NOT_ACCEPT_SUGGEST);
            } else if (suggest.getStatus().equals(Suggest.SuggestStatus.DURING_LESSON)) {
                throw new BusinessLogicException(ExceptionCode.NOT_PAY_SUCCESS);
            } else if (suggest.getStatus().equals(Suggest.SuggestStatus.PAY_IN_PROGRESS)) {
                throw new BusinessLogicException(ExceptionCode.NOT_PAY_SUCCESS);
            }
        }
        String dir = "profileImage";
        String dir1 = "careerImage";
        lessonDbService.deleteLesson(findLesson);
        lessonDbService.deleteLessonInfo(findLessonInfo);
        awsS3Service.delete(findLesson.getProfileImage().getFileName(),dir);
        findLessonInfo.getCareerImages().forEach(careerImage -> awsS3Service.delete(careerImage.getFileName(), dir1));
        lessonDbService.deleteCurriculum(findCurriculum);
    }
}