package com.codueon.boostUp.domain.chat.utils;

import com.codueon.boostUp.domain.chat.dto.RedisChat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class AlarmMessageUtils {

    /**
     * 알람 생성 메서드
     *
     * @param chatRoomId      채팅방 식별자
     * @param memberId        사용자 식별자
     * @param lessonTitle     과외 타이틀
     * @param displayName     닉네임
     * @param attendanceCount 출석일수
     * @param message         메시지
     * @param alarmType       알람 타입
     * @return RedisChat
     * @author mozzi327
     */
    public static RedisChat makeMemberAlarmMessage(Long chatRoomId,
                                                   Long memberId,
                                                   String lessonTitle,
                                                   String displayName,
                                                   Integer attendanceCount,
                                                   String message,
                                                   AlarmType alarmType) {
        switch (alarmType) {
            case JOIN:
                return addJoinMemberCongratulationMessage(chatRoomId, memberId, displayName);
            case ENTER:
                return addEnterChatRoomMessage(chatRoomId, memberId, displayName);
            case REGISTER:
                return addRegisterSuggestMessage(chatRoomId, memberId, lessonTitle, displayName);
            case ACCEPT:
                return addAcceptSuggestMessage(chatRoomId, memberId, lessonTitle, displayName);
            case REJECT:
                return addRejectSuggestMessage(chatRoomId, memberId, lessonTitle, displayName, message);
            case CANCEL:
                return addCancelSuggestMessage(chatRoomId, memberId, lessonTitle, displayName);
            case PAYMENT_SUCCESS:
                return addPaySuccessSuggestMessage(chatRoomId, memberId, lessonTitle, displayName);
            case CHECK_ATTENDANCE:
                return addCheckAttendanceMessage(chatRoomId, memberId, lessonTitle, displayName, attendanceCount);
            case CANCEL_ATTENDANCE:
                return addCancelAttendanceMessage(chatRoomId, memberId, lessonTitle, displayName, attendanceCount);
            case REFUND_REQUEST:
                return addRefundRequestMessage(chatRoomId, memberId, lessonTitle, displayName);
            case ACCEPT_REFUND:
                return addAcceptRefundMessage(chatRoomId, memberId, lessonTitle, attendanceCount);
            case REJECT_REFUND:
                return addRejectRefundMessage(chatRoomId, memberId, lessonTitle, message);
            case END:
                return addEndLessonMessage(chatRoomId, memberId, lessonTitle, displayName);
            case COMPLETED_REVIEW:
                return addCompletedReviewMessage(chatRoomId, memberId, lessonTitle, displayName, message);
            default:
                return addLeaveChatRoomMessage(chatRoomId, memberId, displayName);
        }
    }

    /**
     * 회원가입 시 알람 채팅방 메시지 생성 메서드
     *
     * @param chatRoomId  채팅방 식별자
     * @param memberId    사용자 식별자
     * @param displayName 닉네임
     * @return RedisChat
     * @author mozzi327
     */
    private static RedisChat addJoinMemberCongratulationMessage(Long chatRoomId, Long memberId, String displayName) {
        return RedisChat.builder()
                .chatRoomId(chatRoomId)
                .senderId(memberId)
                .message("[알림] " + displayName + "님! 코듀온에 가입하신 것을 진심으로 환영합니다! ^0^\n"
                        + "지금 과외 선생님을 만나보세요!\n\n"
                        + "- 과외 신청하러가기 : https://codueon.com\n"
                        + "- 과외 등록하러가기 : https://codueon.com\n")
                .displayName("코듀온 알리미")
                .messageType(MessageType.ALARM)
                .createdAt(LocalDateTime.now())
                .build();
    }

    /**
     * 채팅방 생성 시 입장 메시지 생성 메서드
     *
     * @param chatRoomId  채팅방 식별자
     * @param memberId    사용자 식별자
     * @param displayName 닉네임
     * @return RedisChat
     * @author mozzi327
     */
    private static RedisChat addEnterChatRoomMessage(Long chatRoomId, Long memberId, String displayName) {
        return RedisChat.builder()
                .chatRoomId(chatRoomId)
                .senderId(memberId)
                .message("[알림] " + displayName + "님이 입장하셨습니다.")
                .displayName("코듀온 알리미")
                .messageType(MessageType.ALARM)
                .createdAt(LocalDateTime.now())
                .build();
    }

    /**
     * 과외 신청 메시지 생성 메서드
     *
     * @param tutorChatRoomId 채팅방 식별자(선생님)
     * @param tutorId         사용자 식별자(선생님)
     * @param lessonTitle     과외 타이틀
     * @param studentName     닉네임(학생)
     * @return RedisChat
     * @author mozzi327
     */
    private static RedisChat addRegisterSuggestMessage(Long tutorChatRoomId, Long tutorId, String lessonTitle,
                                                       String studentName) {
        return RedisChat.builder()
                .chatRoomId(tutorChatRoomId)
                .senderId(tutorId)
                .message("[알림] " + lessonTitle + "\n"
                        + studentName + "님께서 과외를 신청했어요!")
                .displayName("코듀온 알리미")
                .messageType(MessageType.ALARM)
                .createdAt(LocalDateTime.now())
                .build();
    }

    /**
     * 과외 수락 메시지 생성 메서드
     *
     * @param studentChatRoomId 채팅방 식별자(학생)
     * @param studentId         사용자 식별자(학생)
     * @param lessonTitle       과외 타이틀
     * @param tutorName         닉네임(선생님)
     * @return RedisChat
     * @author mozzi327
     */
    private static RedisChat addAcceptSuggestMessage(Long studentChatRoomId, Long studentId, String lessonTitle,
                                                     String tutorName) {
        return RedisChat.builder()
                .chatRoomId(studentChatRoomId)
                .senderId(studentId)
                .message("[알림] " + lessonTitle + "\n"
                        + tutorName + "님께서 과외를 수락하셨어요!")
                .displayName("코듀온 알리미")
                .messageType(MessageType.ALARM)
                .createdAt(LocalDateTime.now())
                .build();
    }

    /**
     * 과외 거절 메시지 생성 메서드
     *
     * @param studentChatRoomId 채팅방 식별자(학생)
     * @param studentId         사용자 식별자(학생)
     * @param lessonTitle       과외 타이틀
     * @param tutorName         닉네임(선생님)
     * @param rejectMessage     거부사유
     * @return RedisChat
     * @author mozzi327
     */
    private static RedisChat addRejectSuggestMessage(Long studentChatRoomId, Long studentId, String lessonTitle,
                                                     String tutorName, String rejectMessage) {
        return RedisChat.builder()
                .chatRoomId(studentChatRoomId)
                .senderId(studentId)
                .message("[알림] " + lessonTitle + "\n"
                        + tutorName + "님께서 과외 신청을 거절하셨습니다. \n\n"
                        + "거절 사유 : \n"
                        + rejectMessage)
                .displayName("코듀온 알리미")
                .messageType(MessageType.ALARM)
                .createdAt(LocalDateTime.now())
                .build();
    }

    /**
     * 과외 신청 취소 메시지 생성 메서드
     *
     * @param tutorChatRoomId 채팅방 식별자(선생님)
     * @param tutorId         사용자 식별자(선생님)
     * @param lessonTitle     과외 타이틀
     * @param studentName     닉네임(학생)
     * @return RedisChat
     * @author mozzi327
     */
    private static RedisChat addCancelSuggestMessage(Long tutorChatRoomId, Long tutorId, String lessonTitle,
                                                     String studentName) {
        return RedisChat.builder()
                .chatRoomId(tutorChatRoomId)
                .senderId(tutorId)
                .message("[알림] " + lessonTitle + "\n"
                        + studentName + "님께서 과외 신청을 취소하셨어요!")
                .displayName("코듀온 알리미")
                .messageType(MessageType.ALARM)
                .createdAt(LocalDateTime.now())
                .build();
    }

    /**
     * 결제 성공 메시지 생성 메서드
     *
     * @param tutorChatRoomId 채팅방 식별자(선생님)
     * @param tutorId         사용자 식별자(선생님)
     * @param lessonTitle     과외 타이틀
     * @param studentName     닉네임(학생)
     * @return RedisChat
     * @author mozzi327
     */
    private static RedisChat addPaySuccessSuggestMessage(Long tutorChatRoomId, Long tutorId, String lessonTitle,
                                                         String studentName) {
        return RedisChat.builder()
                .chatRoomId(tutorChatRoomId)
                .senderId(tutorId)
                .message("[알림] " + lessonTitle + "\n"
                        + studentName + "님의 과외 결제가 완료되었어요!")
                .displayName("코듀온 알리미")
                .messageType(MessageType.ALARM)
                .createdAt(LocalDateTime.now())
                .build();
    }

    /**
     * 출석 차감 메시지 생성 메서드
     *
     * @param studentChatRoomId 채팅방 식별자(학생)
     * @param studentId         사용자 식별자(학생)
     * @param lessonTitle       과외 타이틀
     * @param studentName       닉네임(학생)
     * @param attendanceCount   출석일수
     * @return RedisChat
     * @author mozzi327
     */
    private static RedisChat addCheckAttendanceMessage(Long studentChatRoomId, Long studentId, String lessonTitle,
                                                       String studentName, Integer attendanceCount) {
        return RedisChat.builder()
                .chatRoomId(studentChatRoomId)
                .senderId(studentId)
                .message("[알림] " + lessonTitle + "\n"
                        + studentName + "님! " + lessonTitle + " 과외 횟수가 " + attendanceCount + "번 남았어요!")
                .displayName("코듀온 알리미")
                .messageType(MessageType.ALARM)
                .createdAt(LocalDateTime.now())
                .build();
    }

    /**
     * 출석 취소 메시지 생성 메서드
     *
     * @param studentChatRoomId 채팅방 식별자(학생)
     * @param studentId         사용자 식별자(학생)
     * @param lessonTitle       과외 타이틀
     * @param studentName       닉네임(학생)
     * @param attendanceCount   출석일수
     * @return RedisChat
     * @author mozzi327
     */
    private static RedisChat addCancelAttendanceMessage(Long studentChatRoomId, Long studentId, String lessonTitle,
                                                        String studentName, Integer attendanceCount) {
        return RedisChat.builder()
                .chatRoomId(studentChatRoomId)
                .senderId(studentId)
                .message("[알림] " + lessonTitle + "\n"
                        + studentName + "님! 과외 선생님께서 출석일수를 수정하셨어요! \n"
                        + "현재 " + lessonTitle + "수업 과외 횟수는 " + attendanceCount + "번 남았어요.")
                .displayName("코듀온 알리미")
                .messageType(MessageType.ALARM)
                .createdAt(LocalDateTime.now())
                .build();
    }

    /**
     * 환불 요청 메시지 생성 메서드
     *
     * @param tutorChatRoomId 채팅방 식별자(선생님)
     * @param tutorId         사용자 식별자(선생님)
     * @param lessonTitle     과외 타이틀
     * @param studentName     닉네임(학생)
     * @return RedisChat
     * @author mozzi327
     */
    private static RedisChat addRefundRequestMessage(Long tutorChatRoomId, Long tutorId, String lessonTitle,
                                                     String studentName) {
        return RedisChat.builder()
                .chatRoomId(tutorChatRoomId)
                .senderId(tutorId)
                .message("[알림] " + lessonTitle + "\n"
                        + studentName + "님께서 과외 환불을 신청하셨어요!")
                .displayName("코듀온 알리미")
                .messageType(MessageType.ALARM)
                .createdAt(LocalDateTime.now())
                .build();
    }

    /**
     * 환불 수락 메시지 생성 메서드
     *
     * @param studentChatRoomId 채팅방 식별자(학생)
     * @param studentId         사용자 식별자(학생)
     * @param lessonTitle       과외 타이틀
     * @return RedisChat
     * @author mozzi327
     */
    private static RedisChat addAcceptRefundMessage(Long studentChatRoomId, Long studentId, String lessonTitle,
                                                    Integer attendanceCount) {
        return RedisChat.builder()
                .chatRoomId(studentChatRoomId)
                .senderId(studentId)
                .message("[알림] " + lessonTitle + "\n"
                        + lessonTitle + "과외 횟수 " + attendanceCount + "회분의 환불 완료되었어요! \n")
                .displayName("코듀온 알리미")
                .messageType(MessageType.ALARM)
                .createdAt(LocalDateTime.now())
                .build();
    }

    /**
     * 환불 거부 메시지 생성 메서드
     *
     * @param studentChatRoomId 채팅방 식별자(학생)
     * @param studentId         사용자 식별자(학생)
     * @param lessonTitle       과외 타이틀
     * @param rejectMessage     거부 사유
     * @return RedisChat
     * @author mozzi327
     */
    private static RedisChat addRejectRefundMessage(Long studentChatRoomId, Long studentId, String lessonTitle,
                                                    String rejectMessage) {
        return RedisChat.builder()
                .chatRoomId(studentChatRoomId)
                .senderId(studentId)
                .message("[알림] " + lessonTitle + "\n"
                        + "환불 신청이 거절됐어요. \n\n"
                        + "거절 사유 : \n"
                        + rejectMessage)
                .displayName("코듀온 알리미")
                .messageType(MessageType.ALARM)
                .createdAt(LocalDateTime.now())
                .build();
    }

    /**
     * 과외 종료 메시지 생성 메서드
     *
     * @param studentChatRoomId 채팅방 식별자(학생)
     * @param studentId         사용자 식별자(학생)
     * @param lessonTitle       과외 타이틀
     * @param tutorName         닉네임(선생님)
     * @return RedisChat
     * @author mozzi327
     */
    private static RedisChat addEndLessonMessage(Long studentChatRoomId, Long studentId, String lessonTitle,
                                                 String tutorName) {
        return RedisChat.builder()
                .chatRoomId(studentChatRoomId)
                .senderId(studentId)
                .message("[알림] " + lessonTitle + "\n"
                        + tutorName + "님의 과외가 종료됐어요! \n\n"
                        + "선생님에 대한 후기를 남겨주세요! \n"
                        + "- 후기 남기러 가기 : https://codueon.com")
                .displayName("코듀온 알리미")
                .messageType(MessageType.ALARM)
                .createdAt(LocalDateTime.now())
                .build();
    }

    /**
     * 리뷰 작성 완료 알람 메시지 생성 메서드
     *
     * @param tutorChatRoomId 채팅방 식별자(선생님)
     * @param tutorId         사용자 식별자(선생님)
     * @param lessonTitle     과외 타이틀
     * @param studentName     닉네임(학생)
     * @return RedisChat
     * @author mozzi327
     */
    private static RedisChat addCompletedReviewMessage(Long tutorChatRoomId, Long tutorId, String lessonTitle,
                                                       String studentName, String message) {
        return RedisChat.builder()
                .chatRoomId(tutorChatRoomId)
                .senderId(tutorId)
                .message("[알림] " + lessonTitle + "\n"
                        + studentName + "님께서 후기를 남겨 주셨어요! \n\n"
                        + message)
                .displayName("코듀온 알리미")
                .messageType(MessageType.ALARM)
                .createdAt(LocalDateTime.now())
                .build();
    }

    /**
     * 채팅방 나가기 메시지 생성 메서드
     *
     * @param chatRoomId  채팅방 식별자
     * @param memberId    사용자 식별자
     * @param displayName 닉네임
     * @return RedisChat
     * @author mozzi327
     */
    private static RedisChat addLeaveChatRoomMessage(Long chatRoomId, Long memberId, String displayName) {
        return RedisChat.builder()
                .chatRoomId(chatRoomId)
                .senderId(memberId)
                .message("[알림] " + displayName + "님이 나가셨습니다.")
                .displayName("코듀온 알리미")
                .messageType(MessageType.ALARM)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
