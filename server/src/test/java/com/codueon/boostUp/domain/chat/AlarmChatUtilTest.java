package com.codueon.boostUp.domain.chat;

import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.utils.AlarmMessageUtils;
import com.codueon.boostUp.domain.chat.utils.AlarmType;
import com.codueon.boostUp.domain.chat.utils.MessageType;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.reveiw.entity.Review;
import com.codueon.boostUp.domain.utils.DataForTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AlarmChatUtilTest {
    protected Member tutor;
    protected Member student;
    protected String lessonTitle;
    protected Long tutorAlarmChatRoomId;
    protected Long studentAlarmChatRoomId;

    @BeforeEach
    void setUp() {
        tutorAlarmChatRoomId = 1L;
        studentAlarmChatRoomId = 2L;
        lessonTitle = "자바 속성 강의!";
        tutor = DataForTest.getSavedTutor();
        student = DataForTest.getSavedStudent();
    }

    @Test
    @DisplayName("AlarmChatUtils 회원 생성 축하 알람 메시지 생성 테스트")
    void makeJoinMemberCongratulationMessageTest() {
        // given
        String message = "[알림] " + student.getName() + "님! 코듀온에 가입하신 것을 진심으로 환영합니다! ^0^\n"
                + "지금 과외 선생님을 만나보세요!\n\n"
                + "- 과외 신청하러가기 : https://codueon.com\n"
                + "- 과외 등록하러가기 : https://codueon.com\n";

        // when
        RedisChat alarmMessage = AlarmMessageUtils
                .makeMemberAlarmMessage(
                        studentAlarmChatRoomId, student.getId(), null,
                        student.getName(), 0, null, AlarmType.JOIN
                );

        // then
        assertThat(alarmMessage.getChatRoomId()).isEqualTo(studentAlarmChatRoomId);
        assertThat(alarmMessage.getSenderId()).isEqualTo(student.getId());
        assertThat(alarmMessage.getMessage()).isEqualTo(message);
        assertThat(alarmMessage.getDisplayName()).isEqualTo("코듀온 알리미");
        assertThat(alarmMessage.getMessageType()).isEqualTo(MessageType.ALARM);
    }

    @Test
    @DisplayName("AlarmChatUtils 채팅방 입장 알람 메시지 생성 테스트")
    void makeEnterChatRoomMessageTest() {
        // given
        String message = "[알림] " + student.getName() + "님이 입장하셨습니다.";

        // when
        RedisChat alarmMessage = AlarmMessageUtils
                .makeMemberAlarmMessage(
                        studentAlarmChatRoomId, student.getId(), null,
                        student.getName(), 0, null, AlarmType.ENTER
                );

        // then
        assertThat(alarmMessage.getChatRoomId()).isEqualTo(studentAlarmChatRoomId);
        assertThat(alarmMessage.getSenderId()).isEqualTo(student.getId());
        assertThat(alarmMessage.getMessage()).isEqualTo(message);
        assertThat(alarmMessage.getDisplayName()).isEqualTo("코듀온 알리미");
        assertThat(alarmMessage.getMessageType()).isEqualTo(MessageType.ALARM);
    }

    @Test
    @DisplayName("AlarmChatUtils 과외 신청 알람 메시지 생성 테스트 <to. 선생님>")
    void makeRegisterSuggestMessageTest() {
        // given
        String message = "[알림] " + lessonTitle + "\n"
                + student.getName() + "님께서 과외를 신청했어요!";

        // when
        RedisChat alarmMessage = AlarmMessageUtils
                .makeMemberAlarmMessage(
                        tutorAlarmChatRoomId, tutor.getId(), lessonTitle,
                        student.getName(), 0, null, AlarmType.REGISTER
                );

        // then
        assertThat(alarmMessage.getChatRoomId()).isEqualTo(tutorAlarmChatRoomId);
        assertThat(alarmMessage.getSenderId()).isEqualTo(tutor.getId());
        assertThat(alarmMessage.getMessage()).isEqualTo(message);
        assertThat(alarmMessage.getDisplayName()).isEqualTo("코듀온 알리미");
        assertThat(alarmMessage.getMessageType()).isEqualTo(MessageType.ALARM);
    }

    @Test
    @DisplayName("AlarmChatUtils 과외 신청 수락 알람 메시지 생성 테스트 <to. 학생>")
    void makeAcceptSuggestMessageTest() {
        // given
        String message = "[알림] " + lessonTitle + "\n"
                + tutor.getName() + "님께서 과외를 수락하셨어요!";

        // when
        RedisChat alarmMessage = AlarmMessageUtils
                .makeMemberAlarmMessage(
                        studentAlarmChatRoomId, student.getId(), lessonTitle,
                        tutor.getName(), 0, null, AlarmType.ACCEPT
                );

        // then
        assertThat(alarmMessage.getChatRoomId()).isEqualTo(studentAlarmChatRoomId);
        assertThat(alarmMessage.getSenderId()).isEqualTo(student.getId());
        assertThat(alarmMessage.getMessage()).isEqualTo(message);
        assertThat(alarmMessage.getDisplayName()).isEqualTo("코듀온 알리미");
        assertThat(alarmMessage.getMessageType()).isEqualTo(MessageType.ALARM);
    }

    @Test
    @DisplayName("AlarmChatUtils 과외 신청 거절 알람 메시지 생성 테스트 <to. 학생>")
    void makeRejectSuggestMessageTest() {
        // given
        String rejectMessage = "너무 띠껍네요 ㅎ";
        String message = "[알림] " + lessonTitle + "\n"
                + tutor.getName() + "님께서 과외 신청을 거절하셨습니다. \n\n"
                + "거절 사유 : \n"
                + rejectMessage;


        // when
        RedisChat alarmMessage = AlarmMessageUtils
                .makeMemberAlarmMessage(
                        studentAlarmChatRoomId, student.getId(), lessonTitle,
                        tutor.getName(), 0, rejectMessage, AlarmType.REJECT
                );

        // then
        assertThat(alarmMessage.getChatRoomId()).isEqualTo(studentAlarmChatRoomId);
        assertThat(alarmMessage.getSenderId()).isEqualTo(student.getId());
        assertThat(alarmMessage.getMessage()).isEqualTo(message);
        assertThat(alarmMessage.getDisplayName()).isEqualTo("코듀온 알리미");
        assertThat(alarmMessage.getMessageType()).isEqualTo(MessageType.ALARM);
    }

    @Test
    @DisplayName("AlarmChatUtils 과외 신청 취소 알람 메시지 생성 테스트 <to. 선생님>")
    void makeCancelSuggestMessageTest() {
        // given
        String message = "[알림] " + lessonTitle + "\n"
                + student.getName() + "님께서 과외 신청을 취소하셨어요!";


        // when
        RedisChat alarmMessage = AlarmMessageUtils
                .makeMemberAlarmMessage(
                        tutorAlarmChatRoomId, tutor.getId(), lessonTitle,
                        student.getName(), 0, null, AlarmType.CANCEL
                );

        // then
        assertThat(alarmMessage.getChatRoomId()).isEqualTo(tutorAlarmChatRoomId);
        assertThat(alarmMessage.getSenderId()).isEqualTo(tutor.getId());
        assertThat(alarmMessage.getMessage()).isEqualTo(message);
        assertThat(alarmMessage.getDisplayName()).isEqualTo("코듀온 알리미");
        assertThat(alarmMessage.getMessageType()).isEqualTo(MessageType.ALARM);
    }

    @Test
    @DisplayName("AlarmChatUtils 과외 결제 완료 알람 메시지 생성 테스트 <to. 선생님>")
    void makePaySuccessMessageTest() {
        // given
        String message = "[알림] " + lessonTitle + "\n"
                + student.getName() + "님의 과외 결제가 완료되었어요!";


        // when
        RedisChat alarmMessage = AlarmMessageUtils
                .makeMemberAlarmMessage(
                        tutorAlarmChatRoomId, tutor.getId(), lessonTitle,
                        student.getName(), 0, null, AlarmType.PAYMENT_SUCCESS
                );

        // then
        assertThat(alarmMessage.getChatRoomId()).isEqualTo(tutorAlarmChatRoomId);
        assertThat(alarmMessage.getSenderId()).isEqualTo(tutor.getId());
        assertThat(alarmMessage.getMessage()).isEqualTo(message);
        assertThat(alarmMessage.getDisplayName()).isEqualTo("코듀온 알리미");
        assertThat(alarmMessage.getMessageType()).isEqualTo(MessageType.ALARM);
    }

    @Test
    @DisplayName("AlarmChatUtils 과외 출석 수 알람 메시지 생성 테스트 <to. 학생>")
    void makeCheckAttendanceTest() {
        // given
        Integer attendanceCount = 4;
        String message = "[알림] " + lessonTitle + "\n"
                + student.getName() + "님! " + lessonTitle + " 과외 횟수가 " + attendanceCount + "번 남았어요!";


        // when
        RedisChat alarmMessage = AlarmMessageUtils
                .makeMemberAlarmMessage(
                        studentAlarmChatRoomId, student.getId(), lessonTitle,
                        student.getName(), attendanceCount, null, AlarmType.CHECK_ATTENDANCE
                );

        // then
        assertThat(alarmMessage.getChatRoomId()).isEqualTo(studentAlarmChatRoomId);
        assertThat(alarmMessage.getSenderId()).isEqualTo(student.getId());
        assertThat(alarmMessage.getMessage()).isEqualTo(message);
        assertThat(alarmMessage.getDisplayName()).isEqualTo("코듀온 알리미");
        assertThat(alarmMessage.getMessageType()).isEqualTo(MessageType.ALARM);
    }

    @Test
    @DisplayName("AlarmChatUtils 과외 출석 취소 알람 메시지 생성 테스트 <to. 학생>")
    void makeCancelAttendanceMessageTest() {
        // given
        Integer attendanceCount = 4;
        String message = "[알림] " + lessonTitle + "\n"
                + student.getName() + "님! 과외 선생님께서 출석일수를 수정하셨어요! \n"
                + "현재 " + lessonTitle + "수업 과외 횟수는 " + attendanceCount + "번 남았어요.";

        // when
        RedisChat alarmMessage = AlarmMessageUtils
                .makeMemberAlarmMessage(
                        studentAlarmChatRoomId, student.getId(), lessonTitle,
                        student.getName(), attendanceCount, null, AlarmType.CANCEL_ATTENDANCE
                );

        // then
        assertThat(alarmMessage.getChatRoomId()).isEqualTo(studentAlarmChatRoomId);
        assertThat(alarmMessage.getSenderId()).isEqualTo(student.getId());
        assertThat(alarmMessage.getMessage()).isEqualTo(message);
        assertThat(alarmMessage.getDisplayName()).isEqualTo("코듀온 알리미");
        assertThat(alarmMessage.getMessageType()).isEqualTo(MessageType.ALARM);
    }

    @Test
    @DisplayName("AlarmChatUtils 과외 환불 요청 알람 메시지 생성 테스트 <to. 선생님>")
    void makeRefundRequestMessageTest() {
        // given
        String message = "[알림] " + lessonTitle + "\n"
                + student.getName() + "님께서 과외 환불을 신청하셨어요!";

        // when
        RedisChat alarmMessage = AlarmMessageUtils
                .makeMemberAlarmMessage(
                        tutorAlarmChatRoomId, tutor.getId(), lessonTitle,
                        student.getName(), 0, null, AlarmType.REFUND_REQUEST
                );

        // then
        assertThat(alarmMessage.getChatRoomId()).isEqualTo(tutorAlarmChatRoomId);
        assertThat(alarmMessage.getSenderId()).isEqualTo(tutor.getId());
        assertThat(alarmMessage.getMessage()).isEqualTo(message);
        assertThat(alarmMessage.getDisplayName()).isEqualTo("코듀온 알리미");
        assertThat(alarmMessage.getMessageType()).isEqualTo(MessageType.ALARM);
    }

    @Test
    @DisplayName("AlarmChatUtils 과외 환불 수락 알람 메시지 생성 테스트 <to. 학생>")
    void makeAcceptRefundMessageTest() {
        // given
        Integer attendanceCount = 4;
        String message = "[알림] " + lessonTitle + "\n"
                + lessonTitle + "과외 횟수 " + attendanceCount + "회분의 환불 완료되었어요! \n";

        // when
        RedisChat alarmMessage = AlarmMessageUtils
                .makeMemberAlarmMessage(
                        studentAlarmChatRoomId, student.getId(), lessonTitle,
                        null, attendanceCount, null, AlarmType.ACCEPT_REFUND
                );

        // then
        assertThat(alarmMessage.getChatRoomId()).isEqualTo(studentAlarmChatRoomId);
        assertThat(alarmMessage.getSenderId()).isEqualTo(student.getId());
        assertThat(alarmMessage.getMessage()).isEqualTo(message);
        assertThat(alarmMessage.getDisplayName()).isEqualTo("코듀온 알리미");
        assertThat(alarmMessage.getMessageType()).isEqualTo(MessageType.ALARM);
    }

    @Test
    @DisplayName("AlarmChatUtils 과외 환불 거절 알람 메시지 생성 테스트 <to. 학생>")
    void makeRejectRefundMessageTest() {
        // given
        String rejectMessage = "선생님이 잘할께요 ㅜㅜ. 제발";
        String message = "[알림] " + lessonTitle + "\n"
                + "환불 신청이 거절됐어요. \n\n"
                + "거절 사유 : \n"
                + rejectMessage;

        // when
        RedisChat alarmMessage = AlarmMessageUtils
                .makeMemberAlarmMessage(
                        studentAlarmChatRoomId, student.getId(), lessonTitle,
                        null, 0, rejectMessage, AlarmType.REJECT_REFUND
                );

        // then
        assertThat(alarmMessage.getChatRoomId()).isEqualTo(studentAlarmChatRoomId);
        assertThat(alarmMessage.getSenderId()).isEqualTo(student.getId());
        assertThat(alarmMessage.getMessage()).isEqualTo(message);
        assertThat(alarmMessage.getDisplayName()).isEqualTo("코듀온 알리미");
        assertThat(alarmMessage.getMessageType()).isEqualTo(MessageType.ALARM);
    }

    @Test
    @DisplayName("AlarmChatUtils 과외 종료 및 리뷰 작성 요청 알람 메시지 생성 테스트 <to. 학생>")
    void makeEndLessonMessageTest() {
        // given
        String message = "[알림] " + lessonTitle + "\n"
                + tutor.getName() + "님의 과외가 종료됐어요! \n\n"
                + "선생님에 대한 후기를 남겨주세요! \n"
                + "- 후기 남기러 가기 : https://codueon.com";

        // when
        RedisChat alarmMessage = AlarmMessageUtils
                .makeMemberAlarmMessage(
                        studentAlarmChatRoomId, student.getId(), lessonTitle,
                        tutor.getName(), 0, null, AlarmType.END
                );

        // then
        assertThat(alarmMessage.getChatRoomId()).isEqualTo(studentAlarmChatRoomId);
        assertThat(alarmMessage.getSenderId()).isEqualTo(student.getId());
        assertThat(alarmMessage.getMessage()).isEqualTo(message);
        assertThat(alarmMessage.getDisplayName()).isEqualTo("코듀온 알리미");
        assertThat(alarmMessage.getMessageType()).isEqualTo(MessageType.ALARM);
    }

    @Test
    @DisplayName("AlarmChatUtils 리뷰 완료 알람 메시지 생성 테스트 <to. 선생님>")
    void makeCompletedReviewMessageTest() {
        // given
        Review review = DataForTest.getReview1();
        String reviewMessage = "평점: " + review.getScore() + "\n"
                + "후기: " + review.getComment();
        String message = "[알림] " + lessonTitle + "\n"
                + student.getName() + "님께서 후기를 남겨 주셨어요! \n\n"
                + reviewMessage;

        // when
        RedisChat alarmMessage = AlarmMessageUtils
                .makeMemberAlarmMessage(
                        tutorAlarmChatRoomId, tutor.getId(), lessonTitle,
                        student.getName(), 0, reviewMessage, AlarmType.COMPLETED_REVIEW
                );

        // then
        assertThat(alarmMessage.getChatRoomId()).isEqualTo(tutorAlarmChatRoomId);
        assertThat(alarmMessage.getSenderId()).isEqualTo(tutor.getId());
        assertThat(alarmMessage.getMessage()).isEqualTo(message);
        assertThat(alarmMessage.getDisplayName()).isEqualTo("코듀온 알리미");
        assertThat(alarmMessage.getMessageType()).isEqualTo(MessageType.ALARM);
    }

    @Test
    @DisplayName("AlarmChatUtils 채팅방 퇴장 알람 메시지 생성 테스트")
    void makeLeaveChatRoomTest() {
        // given
        String message = "[알림] " + student.getName() + "님이 나가셨습니다.";

        // when
        RedisChat alarmMessage = AlarmMessageUtils
                .makeMemberAlarmMessage(
                        studentAlarmChatRoomId, student.getId(), null,
                        student.getName(), 0, null, AlarmType.LEAVE
                );

        // then
        assertThat(alarmMessage.getChatRoomId()).isEqualTo(studentAlarmChatRoomId);
        assertThat(alarmMessage.getSenderId()).isEqualTo(student.getId());
        assertThat(alarmMessage.getMessage()).isEqualTo(message);
        assertThat(alarmMessage.getDisplayName()).isEqualTo("코듀온 알리미");
        assertThat(alarmMessage.getMessageType()).isEqualTo(MessageType.ALARM);
    }
}
