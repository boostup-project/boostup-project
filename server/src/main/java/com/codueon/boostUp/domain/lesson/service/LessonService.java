package com.codueon.boostUp.domain.lesson.service;

import com.codueon.boostUp.domain.bookmark.repository.BookmarkRepository;
import com.codueon.boostUp.domain.lesson.dto.get.*;
import com.codueon.boostUp.domain.lesson.dto.patch.PatchLessonCurriculum;
import com.codueon.boostUp.domain.lesson.dto.patch.PostLessonDetailEdit;
import com.codueon.boostUp.domain.lesson.dto.patch.PostLessonInfoEdit;
import com.codueon.boostUp.domain.lesson.dto.post.PostLesson;
import com.codueon.boostUp.domain.lesson.dto.post.PostSearchLesson;
import com.codueon.boostUp.domain.lesson.entity.*;
import com.codueon.boostUp.domain.lesson.repository.LessonRepository;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.member.service.MemberDbService;
import com.codueon.boostUp.domain.reveiw.service.ReviewDbService;
import com.codueon.boostUp.domain.suggest.entity.Suggest;
import com.codueon.boostUp.domain.suggest.service.SuggestDbService;
import com.codueon.boostUp.global.exception.BusinessLogicException;
import com.codueon.boostUp.global.exception.ExceptionCode;
import com.codueon.boostUp.global.file.AwsS3Service;
import com.codueon.boostUp.global.file.FileHandler;
import com.codueon.boostUp.global.file.UploadFile;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.codueon.boostUp.domain.suggest.entity.SuggestStatus.*;
import static com.codueon.boostUp.global.exception.ExceptionCode.NOT_PAY_IN_PROGRESS;
import static com.codueon.boostUp.global.exception.ExceptionCode.NOT_PAY_SUCCESS;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonDbService lessonDbService;
    private final FileHandler fileHandler;
    private final AwsS3Service awsS3Service;
    private final MemberDbService memberDbService;
    private final SuggestDbService suggestDbService;
    private final ReviewDbService reviewDbService;
    private final BookmarkRepository bookmarkRepository;
    private final LessonRepository lessonRepository;

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
        Lesson savedLesson = saveLessonAndReturnLesson(postLesson, memberId, profileImage);
        saveLessonInfo(savedLesson.getId(), postLesson, careerImage, memberId);
        saveCurriculum(savedLesson.getId(), postLesson, memberId);
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

        if (lessonDbService.ifExistsByMemberId(memberId)) {
            throw new BusinessLogicException(ExceptionCode.LESSON_ALREADY_EXIST);
        }
        Lesson savedLesson = saveLessonAndReturnLessonS3(postLesson, memberId, profileImage);
        saveLessonInfoS3(savedLesson.getId(), postLesson, careerImage, memberId);
        saveCurriculum(savedLesson.getId(), postLesson, memberId);
    }

    /**
     * Lesson을 저장하고 객체를 리턴하는 메서드 (Local)
     *
     * @param postLesson   과외 등록 정보
     * @param memberId     사용자 식별자
     * @param profileImage 프로필 사진
     * @return Lesson
     * @author Quartz614
     */
    @SneakyThrows
    private Lesson saveLessonAndReturnLesson(PostLesson postLesson,
                                             Long memberId,
                                             MultipartFile profileImage) {

        Lesson lesson = Lesson.toEntity(postLesson, memberId);
        UploadFile uploadFile = fileHandler.uploadFile(profileImage);
        ProfileImage createProfileImage = ProfileImage.toEntity(uploadFile, uploadFile.getFilePath());

        lesson.addProfileImage(createProfileImage);

        lessonDbService.addLanguageList(postLesson.getLanguages(), lesson);
        lessonDbService.addAddressList(postLesson.getAddress(), lesson);

        return lessonDbService.returnSavedLesson(lesson);
    }

    /**
     * Lesson을 저장하고 객체를 리턴하는 메서드 (S3)
     *
     * @param postLesson   과외 등록 정보
     * @param memberId     사용자 식별자
     * @param profileImage 프로필 사진
     * @return Lesson
     * @author Quartz614
     */
    @SneakyThrows
    private Lesson saveLessonAndReturnLessonS3(PostLesson postLesson,
                                               Long memberId,
                                               MultipartFile profileImage) {

        Lesson lesson = Lesson.toEntity(postLesson, memberId);

        String dir = "profileImage";
        UploadFile uploadFile = awsS3Service.uploadfile(profileImage, dir);
        ProfileImage createProfileImage = ProfileImage.toEntity(uploadFile, uploadFile.getFilePath());

        lesson.addProfileImage(createProfileImage);

        lessonDbService.addLanguageList(postLesson.getLanguages(), lesson);
        lessonDbService.addAddressList(postLesson.getAddress(), lesson);

        return lessonDbService.returnSavedLesson(lesson);
    }

    /**
     * 과외 디테일 정보 저장 메서드 (Local)
     *
     * @param lessonId    저장 후 조회된 요약 정보
     * @param postLesson  과외 등록 정보
     * @param careerImage 경력 사진
     * @author Quartz614
     */
    @SneakyThrows
    private void saveLessonInfo(Long lessonId,
                                PostLesson postLesson,
                                List<MultipartFile> careerImage,
                                Long memberId) {

        LessonInfo lessonInfo = LessonInfo.toEntity(lessonId, postLesson, memberId);
        List<UploadFile> careerImages = fileHandler.parseUploadFileInfo(careerImage);

        lessonDbService.saveCareerImage(careerImages, lessonInfo);
        lessonDbService.saveLessonInfo(lessonInfo);
    }

    /**
     * 과외 디테일 정보 저장 메서드 (S3)
     *
     * @param lessonId    저장 후 조회된 요약 정보
     * @param postLesson  과외 등록 정보
     * @param careerImage 경력 사진
     * @author Quartz614
     */
    @SneakyThrows
    private void saveLessonInfoS3(Long lessonId,
                                  PostLesson postLesson,
                                  List<MultipartFile> careerImage,
                                  Long memberId) {
        LessonInfo lessonInfo = LessonInfo.toEntity(lessonId, postLesson, memberId);

        String dir = "careerImage";
        List<UploadFile> careerImages = awsS3Service.uploadFileList(careerImage, dir);

        lessonDbService.saveCareerImage(careerImages, lessonInfo);
        lessonDbService.saveLessonInfo(lessonInfo);
    }

    /**
     * 커리큘럼 저장 메서드
     *
     * @param lessonId    과외 식별자
     * @param postLesson  과외 등록 정보
     * @param memberId    사용자 식별자
     * @author Quartz614
     */
    private void saveCurriculum(Long lessonId, PostLesson postLesson, Long memberId) {
        Curriculum curriculum = Curriculum.builder()
                .lessonId(lessonId)
                .curriculum(postLesson.getCurriculum())
                .memberId(memberId)
                .build();

        lessonDbService.saveCurriculum(curriculum);
    }

    /**
     * 과외 요약 정보 수정 메서드 (로컬)
     *
     * @param lessonId           과외 식별자
     * @param memberId           사용자 식별자
     * @param postLessonInfoEdit 수정 과외 요약정보
     * @param profileImage       회원 프로필 이미지
     * @author Quartz614
     */
    @SneakyThrows
    public void updateLessonInfo(Long lessonId,
                                 Long memberId,
                                 PostLessonInfoEdit postLessonInfoEdit,
                                 MultipartFile profileImage) {

        Lesson updateLesson = lessonDbService.ifExistsReturnLessonByLessonIdAndMemberId(lessonId, memberId);
        updateLesson.editLessonInfo(postLessonInfoEdit);

        List<Integer> languageList = postLessonInfoEdit.getLanguages();
        List<Integer> addressList = postLessonInfoEdit.getAddresses();
        lessonDbService.addLanguageList(languageList, updateLesson);
        lessonDbService.addAddressList(addressList, updateLesson);
        String editState = postLessonInfoEdit.getEditState();

        if (editState.equals("true")) {
            UploadFile uploadFile = fileHandler.uploadFile(profileImage);
            ProfileImage newProfileImage = ProfileImage.builder()
                    .originFileName(uploadFile.getOriginFileName())
                    .fileName(uploadFile.getFileName())
                    .filePath(uploadFile.getFilePath())
                    .fileSize(uploadFile.getFileSize())
                    .build();

            updateLesson.addProfileImage(newProfileImage);
        } else if (editState.equals("false")) {
            lessonDbService.saveLesson(updateLesson);
        }
        lessonDbService.saveLesson(updateLesson);
    }

    /**
     * 과외 요약 정보 수정 메서드 (S3)
     *
     * @param lessonId           과외 식별자
     * @param memberId           사용자 식별자
     * @param postLessonInfoEdit 수정 과외 요약정보
     * @param profileImage       회원 프로필 이미지
     * @author Quartz614
     */
    @SneakyThrows
    public void updateLessonInfoS3(Long lessonId,
                                   Long memberId,
                                   PostLessonInfoEdit postLessonInfoEdit,
                                   MultipartFile profileImage) {

        Lesson updateLesson = lessonDbService.ifExistsReturnLessonByLessonIdAndMemberId(lessonId, memberId);

        updateLesson.editLessonInfo(postLessonInfoEdit);
        ProfileImage profileImage1 = updateLesson.getProfileImage();

        String dir = "profileImage";
        awsS3Service.delete(profileImage1.getFileName(), dir);

        List<Integer> languageList = postLessonInfoEdit.getLanguages();
        List<Integer> addressList = postLessonInfoEdit.getAddresses();

        lessonDbService.addLanguageList(languageList, updateLesson);
        lessonDbService.addAddressList(addressList, updateLesson);
        String editState = postLessonInfoEdit.getEditState();

        if (editState.equals("true")) {
            UploadFile uploadFile = awsS3Service.uploadfile(profileImage, dir);
            ProfileImage editProfileImage = ProfileImage.builder()
                    .originFileName(uploadFile.getOriginFileName())
                    .fileName(uploadFile.getFileName())
                    .filePath(uploadFile.getFilePath())
                    .fileSize(uploadFile.getFileSize())
                    .build();

            updateLesson.addProfileImage(editProfileImage);
        } else if (editState.equals("false")) {
            lessonDbService.saveLesson(updateLesson);
        }
        lessonDbService.saveLesson(updateLesson);
    }

    /**
     * 과외 상세 정보 수정 메서드 (로컬)
     *
     * @param lessonId             과외 식별자
     * @param memberId             사용자 식별자
     * @param postLessonDetailEdit 수정 과외 상세정보
     * @param careerImage          경력 이미지
     * @author Quartz614
     */
    @SneakyThrows
    public void updateLessonDetail(Long lessonId,
                                   Long memberId,
                                   PostLessonDetailEdit postLessonDetailEdit,
                                   List<MultipartFile> careerImage) {

        LessonInfo updateLessonDetail = lessonDbService.ifExistsReturnLessonInfoByLessonIdAndMemberId(lessonId, memberId);
        updateLessonDetail.editLessonDetail(postLessonDetailEdit);

        List<Long> careerImages = postLessonDetailEdit.getCareerImages();
        List<CareerImage> careerImageList = new ArrayList<>(updateLessonDetail.getCareerImages());
        if (careerImageList.size() == 0) {
            lessonDbService.saveLessonInfo(updateLessonDetail);
        } else {
            for (Long image : careerImages) {
                for (int j = 0; j < careerImageList.size(); j++) {
                    if (Objects.equals(careerImageList.get(j).getId(), image)) {
                        careerImageList.remove(j);
                        break;
                    }
                }
            }
        }

        List<UploadFile> uploadFileList = fileHandler.parseUploadFileInfo(careerImage);
        updateLessonDetail.editCareerImage(careerImageList);

        lessonDbService.editCareerImage(uploadFileList, updateLessonDetail);
        lessonDbService.saveLessonInfo(updateLessonDetail);
    }

    /**
     * 과외 상세 정보 수정 메서드 (S3)
     *
     * @param lessonId             과외 식별자
     * @param memberId             사용자 식별자
     * @param postLessonDetailEdit 수정 과외 상세정보
     * @param careerImage          경력 이미지
     * @author Quartz614
     */
    @SneakyThrows
    public void updateLessonDetailS3(Long lessonId,
                                     Long memberId,
                                     PostLessonDetailEdit postLessonDetailEdit,
                                     List<MultipartFile> careerImage) {

        LessonInfo updateLessonDetail = lessonDbService.ifExistsReturnLessonInfoByLessonIdAndMemberId(lessonId, memberId);
        updateLessonDetail.editLessonDetail(postLessonDetailEdit);

        List<Long> careerImages = postLessonDetailEdit.getCareerImages();
        List<CareerImage> careerImageList = new ArrayList<>(updateLessonDetail.getCareerImages());

        String dir = "careerImage";
        if (careerImageList.size() == 0) {
            lessonDbService.saveLessonInfo(updateLessonDetail);
        } else {
            for (Long image : careerImages) {
                for (int j = 0; j < careerImageList.size(); j++) {
                    if (Objects.equals(careerImageList.get(j).getId(), image)) {
                        awsS3Service.delete(careerImageList.get(j).getFilePath(), dir);
                        careerImageList.remove(j);
                        break;
                    }
                }
            }
        }

        List<UploadFile> uploadFileList = awsS3Service.uploadFileList(careerImage, dir);
        updateLessonDetail.editCareerImage(careerImageList);
        lessonDbService.editCareerImage(uploadFileList, updateLessonDetail);
        lessonDbService.saveLessonInfo(updateLessonDetail);
    }

    /**
     * 커리큘럼 수정 메서드
     *
     * @param lessonId              과외 식별자
     * @param memberId              사용자 식별자
     * @param patchLessonCurriculum 수정 커리큘럼 정보
     * @author Quartz614
     */
    @SneakyThrows
    public void updateCurriculum(Long lessonId,
                                 Long memberId,
                                 PatchLessonCurriculum patchLessonCurriculum) {

        Curriculum updateCurriculum = lessonDbService.ifExistsReturnCurriculumByLessonIdAndMemberId(lessonId, memberId);

        updateCurriculum.editCurriculum(patchLessonCurriculum);
        lessonDbService.editCurriculum(updateCurriculum);
    }

    /**
     * 과외 삭제 메서드 (로컬)
     *
     * @param memberId 회원 식별자
     * @param lessonId 과외 식별자
     * @author Quartz614
     */
    @Transactional
    public void deleteLesson(Long memberId, Long lessonId) {
        Lesson findLesson = lessonDbService.ifExistsReturnLessonByLessonIdAndMemberId(lessonId, memberId);
        List<Suggest> findSuggest = suggestDbService.findAllSuggestForLesson(lessonId);

        for (Suggest suggest : findSuggest) {
            if (suggest.getSuggestStatus().equals(ACCEPT_IN_PROGRESS)) {
                throw new BusinessLogicException(NOT_PAY_IN_PROGRESS);
            } else if (suggest.getSuggestStatus().equals(DURING_LESSON)) {
                throw new BusinessLogicException(NOT_PAY_SUCCESS);
            } else if (suggest.getSuggestStatus().equals(PAY_IN_PROGRESS)) {
                throw new BusinessLogicException(NOT_PAY_SUCCESS);
            }
        }

        LessonInfo findLessonInfo = lessonDbService.ifExistsReturnLessonInfo(lessonId);

        reviewDbService.removeAllByReviews(lessonId);
        bookmarkRepository.deleteByLessonId(lessonId);
        fileHandler.delete(findLesson.getProfileImage().getFilePath());
        findLessonInfo.getCareerImages().forEach(careerImage -> fileHandler.delete(careerImage.getFilePath()));
        lessonDbService.deleteLesson(findLesson);
        lessonDbService.deleteLessonInfo(findLessonInfo);
        lessonDbService.deleteCurriculum(lessonId);
    }

    /**
     * 과외 삭제 메서드 (S3)
     *
     * @param memberId 회원 식별자
     * @param lessonId 과외 식별자
     * @author Quartz614
     */
    @Transactional
    public void deleteLessonS3(Long memberId, Long lessonId) {
        Lesson findLesson = lessonDbService.ifExistsReturnLesson(lessonId);
        Member findMember = memberDbService.ifExistsReturnMember(findLesson.getMemberId());

        if (!findMember.getId().equals(memberId)) {
            throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED_FOR_DELETE);
        }

        List<Suggest> findSuggest = suggestDbService.findAllSuggestForLesson(lessonId);

        for (Suggest suggest : findSuggest) {
            if (suggest.getSuggestStatus().equals(ACCEPT_IN_PROGRESS)) {
                throw new BusinessLogicException(NOT_PAY_IN_PROGRESS);
            } else if (suggest.getSuggestStatus().equals(DURING_LESSON)) {
                throw new BusinessLogicException(NOT_PAY_SUCCESS);
            } else if (suggest.getSuggestStatus().equals(PAY_IN_PROGRESS)) {
                throw new BusinessLogicException(NOT_PAY_SUCCESS);
            }
        }

        LessonInfo findLessonInfo = lessonDbService.ifExistsReturnLessonInfo(lessonId);

        reviewDbService.removeAllByReviews(lessonId);
        bookmarkRepository.deleteByLessonId(lessonId);

        String dir = "profileImage";
        String dir1 = "careerImage";

        awsS3Service.delete(findLesson.getProfileImage().getFileName(), dir);
        findLessonInfo.getCareerImages().forEach(careerImage -> awsS3Service.delete(careerImage.getFileName(), dir1));

        lessonDbService.deleteLesson(findLesson);
        lessonDbService.deleteLessonInfo(findLessonInfo);
        lessonDbService.deleteCurriculum(lessonId);
    }

    /**
     * 마이페이지 등록한 과외 조회 메서드
     *
     * @param memberId 사용자 식별자
     * @return lessonId 과외 식별자
     * @author Quartz614
     */
    public Long getLessonMyPage(Long memberId) {
        return lessonDbService.ifExistsReturnLessonByMemberId(memberId).getId();
    }

    /**
     * 메인페이지 조회 메서드
     *
     * @param memberId 사용자 식별자
     * @param pageable 페이지 정보
     * @return Page(GetMainPageLesson)
     * @author mozzi327
     */
    public Page<GetMainPageLesson> getMainPageLessons(Long memberId, Pageable pageable) {

        if (memberId == null) return lessonRepository.getMainPageLessons(pageable);
        return lessonRepository.getMainPageLessonsAndBookmarkInfo(memberId, pageable);
    }

    /**
     * 메인페이지 상세 검색 메서드
     *
     * @param memberId         사용자 식별자
     * @param postSearchLesson 상세 검색 정보
     * @param pageable         페이지 정보
     * @return Page(GetMainPageLesson)
     * @author mozzi327
     */
    public Page<GetMainPageLesson> getDetailSearchLessons(Long memberId,
                                                          PostSearchLesson postSearchLesson,
                                                          Pageable pageable) {

        if (memberId != null)
            return lessonRepository.getDetailSearchMainPageLessonAndGetBookmarkInfo(memberId, postSearchLesson, pageable);
        return lessonRepository.getDetailSearchMainPageLesson(postSearchLesson, pageable);
    }

    /**
     * 메인페이지 언어 별 과외 조회 메서드
     *
     * @param languageId 사용 언어 식별자
     * @param pageable   페이지 정보
     * @return Page(GetMainPageLesson)
     * @author mozzi327
     */
    public Page<GetMainPageLesson> getMainPageLessonsAboutLanguage(Long memberId,
                                                                   Integer languageId,
                                                                   Pageable pageable) {

        if (memberId != null) return lessonRepository.getMainPageLessonByLanguageAndBookmarkInfo(memberId, languageId, pageable);
        return lessonRepository.getMainPageLessonByLanguage(languageId, pageable);
    }

    /**
     * 과외 상세페이지 요약 정보 조회 메서드
     *
     * @param lessonId 과외 식별자
     * @param memberId 사용자 식별자
     * @return GetLesson
     * @author mozzi327
     */
    public GetLesson getDetailLesson(Long lessonId, Long memberId) {
        if (memberId == null) return lessonRepository.getDetailLesson(lessonId);
        return lessonRepository.getDetailLessonForEditable(lessonId, memberId);
    }

    /**
     * 과외 상세페이지 상세 정보 조회 메서드
     *
     * @param lessonId 과외 식별자
     * @return GetLessonInfo
     * @author mozzi327
     */
    public GetLessonInfo getDetailLessonInfo(Long lessonId) {

        LessonInfo lessonInfo = lessonDbService.ifExistsReturnLessonInfo(lessonId);
        return GetLessonInfo.builder()
                .lessonInfo(lessonInfo)
                .build();
    }

    /**
     * 과외 상세페이지 커리큘럼 정보 조회 메서드
     *
     * @param lessonId 과외 식별자
     * @return GetLessonCurriculum
     * @author mozzi327
     */
    public GetLessonCurriculum getDetailLessonCurriculum(Long lessonId) {

        Curriculum findCurriculum = lessonDbService.ifExistsReturnCurriculum(lessonId);
        return GetLessonCurriculum.builder()
                .curriculum(findCurriculum.getCurriculum())
                .build();
    }

    /**
     * 선생님 자신의 과외 요약 정보를 조회하는 메서드
     *
     * @param memberId 사용자 식별자
     * @return GetLesson
     * @author mozzi327
     */
    public GetTutorLesson getMyLesson(Long memberId) {
        return GetTutorLesson.builder()
                .lesson(lessonDbService.ifExistsReturnLessonByMemberId(memberId))
                .build();
    }
}
