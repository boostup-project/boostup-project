package com.codueon.boostUp.domain.suggest.service;

import com.codueon.boostUp.domain.chat.utils.AlarmType;
import com.codueon.boostUp.domain.lesson.dto.get.GetLessonInfoForAlarm;
import com.codueon.boostUp.domain.lesson.entity.Lesson;
import com.codueon.boostUp.domain.lesson.service.LessonDbService;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.member.service.MemberDbService;
import com.codueon.boostUp.domain.reveiw.service.ReviewService;
import com.codueon.boostUp.domain.suggest.dto.GetLessonAttendance;
import com.codueon.boostUp.domain.suggest.dto.PostReason;
import com.codueon.boostUp.domain.suggest.dto.PostSuggest;
import com.codueon.boostUp.domain.suggest.entity.PaymentInfo;
import com.codueon.boostUp.domain.suggest.entity.Reason;
import com.codueon.boostUp.domain.suggest.entity.Suggest;
import com.codueon.boostUp.domain.suggest.entity.SuggestStatus;
import com.codueon.boostUp.domain.vo.AuthVO;
import com.codueon.boostUp.global.exception.BusinessLogicException;
import com.codueon.boostUp.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.codueon.boostUp.domain.suggest.entity.SuggestStatus.*;
import static com.codueon.boostUp.global.exception.ExceptionCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SuggestService {
    private final ReviewService reviewService;
    private final LessonDbService lessonDbService;
    private final MemberDbService memberDbService;
    private final SuggestDbService suggestDbService;
    private final SuggestEventService suggestEventService;

    /**
     * 신청 생성 메서드
     *
     * @param post     신청 생성 DTO
     * @param lessonId 과외 식별자
     * @param authInfo 사용자 인증 정보
     * @author LeeGoh
     */
    @Transactional
    public void createSuggest(PostSuggest post, Long lessonId, AuthVO authInfo) {
        if(authInfo.getMemberId().equals(lessonDbService.getMemberIdByLessonId(lessonId))) {
            throw new BusinessLogicException(ExceptionCode.TUTOR_CANNOT_RESERVATION);
        }
        suggestDbService.ifUnfinishedSuggestExistsReturnException(lessonId, authInfo.getMemberId());

        Suggest suggest = Suggest.builder()
                .days(post.getDays())
                .languages(post.getLanguages())
                .requests(post.getRequests())
                .lessonId(lessonId)
                .memberId(authInfo.getMemberId())
                .build();

        suggest.setStatus(ACCEPT_IN_PROGRESS);
        suggestDbService.saveSuggest(suggest);

        // 최초 채팅방 생성 이벤트
        suggestEventService.sendMakeChatRoom(authInfo, lessonId);
        // 채팅방 목록, 메시지 알람 최신화 이벤트
        GetLessonInfoForAlarm tutorInfo = lessonDbService.getLessonInfoForAlarm(lessonId);
        suggestEventService.sendAlarmMessage(tutorInfo.getTutorId(), tutorInfo.getTitle(), authInfo.getName(), null, null, AlarmType.REGISTER);
    }

    /**
     * 신청 수락 메서드
     *
     * @param suggestId 신청 식별자
     * @param authInfo  사용자 인증 정보
     * @param quantity  과외 횟수
     * @author LeeGoh
     */
    @Transactional
    public void acceptSuggest(Long suggestId, AuthVO authInfo, Integer quantity) {
        Suggest findSuggest = suggestDbService.ifExistsReturnSuggest(suggestId);
        Lesson findLesson = lessonDbService.ifExistsReturnLesson(findSuggest.getLessonId());

        if (!authInfo.getMemberId().equals(findLesson.getMemberId()))
            throw new BusinessLogicException(INVALID_ACCESS);

        if (!findSuggest.getSuggestStatus().equals(ACCEPT_IN_PROGRESS))
            throw new BusinessLogicException(NOT_ACCEPT_IN_PROGRESS);

        PaymentInfo paymentInfo = PaymentInfo.builder()
                .quantity(quantity)
                .build();
        paymentInfo.setQuantityCount(0);
        paymentInfo.setSuggest(findSuggest);
        suggestDbService.savePayment(paymentInfo);

        findSuggest.setStartTime();
        findSuggest.setTotalCost(findLesson.getCost() * paymentInfo.getQuantity());
        findSuggest.setStatus(PAY_IN_PROGRESS);
        suggestDbService.saveSuggest(findSuggest);

        GetLessonInfoForAlarm tutorInfo = lessonDbService.getLessonInfoForAlarm(findSuggest.getLessonId());
        suggestEventService.sendAlarmMessage(findSuggest.getMemberId(), tutorInfo.getTitle(), tutorInfo.getTutorName(), null, null, AlarmType.ACCEPT);
    }

    /**
     * 신청 취소 메서드
     *
     * @param suggestId 신청 식별자
     * @param authInfo  사용자 인증 정보
     * @author LeeGoh
     */
    public void cancelSuggest(Long suggestId, AuthVO authInfo) {
        Suggest findSuggest = suggestDbService.ifExistsReturnSuggest(suggestId);

        if (!authInfo.getMemberId().equals(findSuggest.getMemberId())) {
            throw new BusinessLogicException(UNAUTHORIZED_FOR_DELETE);
        }

        if (!findSuggest.getSuggestStatus().equals(ACCEPT_IN_PROGRESS) &&
                !findSuggest.getSuggestStatus().equals(PAY_IN_PROGRESS)) {
            throw new BusinessLogicException(NOT_SUGGEST_OR_NOT_ACCEPT);
        }

        suggestDbService.deleteSuggest(findSuggest);
        GetLessonInfoForAlarm tutorInfo = lessonDbService.getLessonInfoForAlarm(findSuggest.getLessonId());
        suggestEventService.sendAlarmMessage(tutorInfo.getTutorId(), tutorInfo.getTitle(), authInfo.getName(), null, null, AlarmType.CANCEL);
    }

    /**
     * 신청 거절 메서드
     *
     * @param suggestId  신청 식별자
     * @param authInfo   사용자 인증 정보
     * @param postReason 거절 사유
     * @author LeeGoh
     */
    public void declineSuggest(Long suggestId, AuthVO authInfo, PostReason postReason) {
        Suggest findSuggest = suggestDbService.ifExistsReturnSuggest(suggestId);

        if (!authInfo.getMemberId().equals(lessonDbService.getMemberIdByLessonId(findSuggest.getLessonId())))
            throw new BusinessLogicException(INVALID_ACCESS);

        if (!findSuggest.getSuggestStatus().equals(ACCEPT_IN_PROGRESS) &&
            !findSuggest.getSuggestStatus().equals(PAY_IN_PROGRESS))
            throw new BusinessLogicException(NOT_SUGGEST_OR_NOT_ACCEPT);

        Reason reason = Reason.builder().reason(postReason.getReason()).build();
        suggestDbService.saveReason(reason);

        suggestDbService.deleteSuggest(findSuggest);

        GetLessonInfoForAlarm tutorInfo = lessonDbService.getLessonInfoForAlarm(findSuggest.getLessonId());
        suggestEventService.sendAlarmMessage(findSuggest.getMemberId(), tutorInfo.getTitle(), tutorInfo.getTutorName(), null, reason.getReason(), AlarmType.REJECT);
    }

    /**
     * 과외 종료 시 신청 상태 및 종료 시간 저장 메서드
     *
     * @param suggestId 신청 식별자
     * @author LeeGoh
     */
    public void setSuggestStatusAndEndTime(Long suggestId, Long memberId) {
        Suggest findSuggest = suggestDbService.ifExistsReturnSuggest(suggestId);

        if (!lessonDbService.getMemberIdByLessonId(findSuggest.getLessonId()).equals(memberId))
            throw new BusinessLogicException(INVALID_ACCESS);

        if (!findSuggest.getSuggestStatus().equals(DURING_LESSON))
            throw new BusinessLogicException(NOT_DURING_LESSON);

        findSuggest.setStatus(END_OF_LESSON);
        findSuggest.setEndTime();
        suggestDbService.saveSuggest(findSuggest);

        GetLessonInfoForAlarm tutorInfo = lessonDbService.getLessonInfoForAlarm(findSuggest.getLessonId());
        suggestEventService.sendAlarmMessage(findSuggest.getMemberId(), tutorInfo.getTitle(), tutorInfo.getTutorName(), null, null, AlarmType.END);
    }

    /**
     * 강사 종료 과외 삭제 메서드
     *
     * @param suggestId 신청 식별자
     * @param memberId  사용된 식별자
     * @author LeeGoh
     */
    public void deleteTutorEndOfSuggest(Long suggestId, Long memberId) {
        Suggest findSuggest = suggestDbService.ifExistsReturnSuggest(suggestId);

        if (!memberId.equals(lessonDbService.getMemberIdByLessonId(findSuggest.getLessonId())))
            throw new BusinessLogicException(UNAUTHORIZED_FOR_DELETE);

        deleteReviewAndSuggest(findSuggest);
    }

    /**
     * 학생 종료 과외 삭제 메서드
     *
     * @param suggestId 신청 식별자
     * @param memberId  사용된 식별자
     * @author LeeGoh
     */
    public void deleteStudentEndOfSuggest(Long suggestId, Long memberId) {
        Suggest findSuggest = suggestDbService.ifExistsReturnSuggest(suggestId);

        if (!memberId.equals(findSuggest.getMemberId()))
            throw new BusinessLogicException(UNAUTHORIZED_FOR_DELETE);

        deleteReviewAndSuggest(findSuggest);
    }

    /**
     * 종료 과외 삭제 공통 메서드
     *
     * @param suggest 신청 정보
     * @author LeeGoh
     */
    private void deleteReviewAndSuggest(Suggest suggest) {
        if (!suggest.getSuggestStatus().equals(END_OF_LESSON))
            throw new BusinessLogicException(NOT_SUGGEST_OR_NOT_ACCEPT);

        reviewService.removeReviewBySuggestId(suggest.getId());
        suggestDbService.deleteSuggest(suggest);
    }

    /*--------------------------------------- 출석부 메서드 --------------------------------------*/

    /**
     * 출석 인정 메서드
     *
     * @param suggestId 신청 식별자
     * @param memberId  회원 식별자
     * @return Integer
     * @author LeeGoh
     */
    public Integer teacherChecksAttendance(Long suggestId, Long memberId) {
        Suggest findSuggest = suggestDbService.ifExistsReturnSuggest(suggestId);
        Long tutorMemberId = lessonDbService.getMemberIdByLessonId(findSuggest.getLessonId());
        lessonGetMemberIdAndStatusIsDuringLesson(findSuggest.getSuggestStatus(), tutorMemberId, memberId);

        PaymentInfo findPaymentInfo = suggestDbService.ifExistsReturnPaymentInfo(suggestId);
        suggestDbService.checkQuantityCount(findPaymentInfo);
        Integer attendanceCount = findPaymentInfo.getQuantityCount();

        Member student = memberDbService.ifExistsReturnMember(findSuggest.getMemberId());
        GetLessonInfoForAlarm tutorInfo = lessonDbService.getLessonInfoForAlarm(findSuggest.getLessonId());
        suggestEventService.sendAlarmMessage(student.getId(), tutorInfo.getTitle(), student.getName(), attendanceCount, null, AlarmType.CHECK_ATTENDANCE);
        return attendanceCount;
    }

    /**
     * 출석 인정 취소 메서드
     *
     * @param suggestId 신청 식별자
     * @param memberId  회원 식별자
     * @return Integer
     * @author LeeGoh
     */
    public Integer teacherCancelAttendance(Long suggestId, Long memberId) {
        Suggest findSuggest = suggestDbService.ifExistsReturnSuggest(suggestId);
        Long tutorMemberId = lessonDbService.getMemberIdByLessonId(findSuggest.getLessonId());
        lessonGetMemberIdAndStatusIsDuringLesson(findSuggest.getSuggestStatus(), tutorMemberId, memberId);

        PaymentInfo findPaymentInfo = suggestDbService.ifExistsReturnPaymentInfo(suggestId);
        suggestDbService.cancelQuantityCount(findSuggest.getPaymentInfo());

        Integer attendanceCount = findPaymentInfo.getQuantityCount();

        Member student = memberDbService.ifExistsReturnMember(findSuggest.getMemberId());
        GetLessonInfoForAlarm tutorInfo = lessonDbService.getLessonInfoForAlarm(findSuggest.getLessonId());
        suggestEventService.sendAlarmMessage(student.getId(), tutorInfo.getTitle(), student.getName(), attendanceCount, null, AlarmType.CANCEL_ATTENDANCE);

        return attendanceCount;
    }

    /**
     * 출석부 조회 메서드
     *
     * @param suggestId 신청 식별자
     * @param memberId  회원 식별자
     * @return Integer
     * @author LeeGoh
     */
    public GetLessonAttendance getLessonAttendance(Long suggestId, Long memberId) {
        Suggest findSuggest = suggestDbService.ifExistsReturnSuggest(suggestId);
        suggestGetMemberIdAndStatusIsNotInProgress(findSuggest, memberId);
        PaymentInfo findPaymentInfo = suggestDbService.ifExistsReturnPaymentInfo(suggestId);

        Integer quantity = findPaymentInfo.getQuantity();
        Integer quantityCount = findPaymentInfo.getQuantityCount();

        return GetLessonAttendance.builder()
                .quantity(quantity)
                .quantityCount(quantityCount)
                .progress((int) ((double) quantityCount / (double) quantity * 100))
                .build();
    }

    /*--------------------------------------- 예외처리 메서드 --------------------------------------*/

    /**
     * 예외처리 - Suggest memberId, status 비교
     *
     * @param suggest 신청 정보
     * @param memberId 회원 식별자
     * @author LeeGoh
     */
    public void suggestGetMemberIdAndStatusIsDuringLesson(Suggest suggest, Long memberId) {
        if (!suggest.getMemberId().equals(memberId))
            throw new BusinessLogicException(INVALID_ACCESS);

        if (!suggest.getSuggestStatus().equals(DURING_LESSON))
            throw new BusinessLogicException(NOT_DURING_LESSON);
    }

    /**
     * 예외처리 - Lesson memberId, Suggest status 비교
     *
     * @param status 신청 상태
     * @param tutorMemberId 과외 등록한 사용자 식별자
     * @param memberId 회원 식별자
     * @author LeeGoh
     */
    public void lessonGetMemberIdAndStatusIsDuringLesson(SuggestStatus status, Long tutorMemberId, Long memberId) {
        if (!tutorMemberId.equals(memberId))
            throw new BusinessLogicException(INVALID_ACCESS);

        if (!status.equals(DURING_LESSON)) {
            throw new BusinessLogicException(NOT_DURING_LESSON);
        }
    }

    /**
     * 예외처리 - Suggest memberId, Suggest status 두 가지 비교
     *
     * @param suggest 신청 정보
     * @param memberId 회원 식별자
     * @author LeeGoh
     */
    public void suggestGetMemberIdAndStatusIsNotInProgress(Suggest suggest, Long memberId) {
        if (!suggest.getMemberId().equals(memberId))
            throw new BusinessLogicException(INVALID_ACCESS);

        if (suggest.getSuggestStatus().equals(ACCEPT_IN_PROGRESS) ||
                suggest.getSuggestStatus().equals(PAY_IN_PROGRESS))
            throw new BusinessLogicException(NOT_SUGGEST_OR_NOT_ACCEPT);
    }
}
