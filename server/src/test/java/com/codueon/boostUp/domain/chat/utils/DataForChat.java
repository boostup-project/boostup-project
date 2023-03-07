package com.codueon.boostUp.domain.chat.utils;

import com.codueon.boostUp.domain.chat.dto.PostMessage;
import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.entity.ChatRoom;
import com.codueon.boostUp.domain.vo.AuthInfo;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class DataForChat {
    /* --------------------------------      공통       -------------------------------- */

    public static final String CODUEON_ALARM = "코듀온 알리미";
    public static final String LESSON_TITLE = "자바 속성 강의!";
    public static final Long CHAT_ROOM_ID1 = 1L;
    public static final Long CHAT_ROOM_ID2 = 2L;
    public static final Long TUTOR_ID = 1L;
    public static final String TUTOR_NAME = "선생이에요";
    public static final Long STUDENT_ID = 2L;
    public static final String STUDENT_NAME = "학생이에요";
    public static final String TUTOR_MESSAGE = "어 그래";
    public static final String STUDENT_MESSAGE = "안녕하세요";
    public static final String REJECT_MESSAGE = "띠껍네요";
    public static final String REVIEW_MESSAGE = "평점: 4.0 \n" +
                                                "후기: 거지같았어요!";
    public static final Long TUTOR_CHAT_ROOM_ID = 1L;
    public static final Long STUDENT_CHAT_ROOM_ID = 2L;
    public static final String EXPIRED_ACCESS_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6WyJVU0VSIl0sIm5hbWUiOiLshKDsg53snbTsl5DsmpQiLCJpZCI6MSwic3ViIjoidHV0b3JAZ21haWwuY29tIiwiaWF0IjoxNjc3NDk5NDU1LCJleHAiOjE2Nzc0OTk1MTV9.Ix2dqcn0lA-iTeF1LypNQe27AjlYWauF28Z_KoPQJuC5l1jmyUvWG2Nn3v3NVh-eVMiKNFcdXExZWdWhJLiwPQ";

    /* --------------------------------      선생님      -------------------------------- */


    public static final AuthInfo TUTOR_INFO = AuthInfo.builder()
            .memberId(TUTOR_ID)
            .name(TUTOR_NAME)
            .build();

    public static final PostMessage TUTOR_SEND_MESSAGE = PostMessage.builder()
            .senderId(TUTOR_ID)
            .receiverId(STUDENT_ID)
            .chatRoomId(CHAT_ROOM_ID1)
            .messageContent(TUTOR_MESSAGE)
            .build();

    public static final RedisChat TUTOR_CHAT = RedisChat.builder()
            .chatRoomId(CHAT_ROOM_ID1)
            .senderId(TUTOR_ID)
            .displayName(TUTOR_NAME)
            .message(TUTOR_MESSAGE)
            .createdAt(LocalDateTime.now())
            .messageType(MessageType.TALK)
            .build();

    public static final RedisChat TUTOR_ENTER_CHAT = AlarmMessageUtils
            .makeMemberAlarmMessage(
                    CHAT_ROOM_ID1, TUTOR_ID, null, TUTOR_NAME, 0, null, AlarmType.ENTER
            );

    public static final RedisChat TUTOR_LEAVE_CHAT = AlarmMessageUtils
            .makeMemberAlarmMessage(
                    CHAT_ROOM_ID1, TUTOR_ID, null, TUTOR_NAME, 0, null, AlarmType.LEAVE
            );


    /* --------------------------------      학생      -------------------------------- */


    public static final AuthInfo STUDENT_INFO = AuthInfo.builder()
            .memberId(STUDENT_ID)
            .name(STUDENT_NAME)
            .build();

    public static final PostMessage STUDENT_SEND_MESSAGE = PostMessage.builder()
            .senderId(STUDENT_ID)
            .receiverId(TUTOR_ID)
            .chatRoomId(CHAT_ROOM_ID1)
            .messageContent(STUDENT_MESSAGE)
            .build();

    public static final RedisChat STUDENT_CHAT = RedisChat.builder()
            .chatRoomId(CHAT_ROOM_ID1)
            .senderId(STUDENT_ID)
            .displayName(STUDENT_NAME)
            .message(STUDENT_MESSAGE)
            .createdAt(LocalDateTime.now().minusHours(1L))
            .messageType(MessageType.TALK)
            .build();

    public static final RedisChat STUDENT_ENTER_CHAT = AlarmMessageUtils
            .makeMemberAlarmMessage(
                    CHAT_ROOM_ID1, STUDENT_ID, null, STUDENT_NAME, 0, null, AlarmType.ENTER
            );

    public static final RedisChat STUDENT_LEAVE_CHAT = AlarmMessageUtils
            .makeMemberAlarmMessage(
                    CHAT_ROOM_ID1, STUDENT_ID, null, STUDENT_NAME, 0, null, AlarmType.LEAVE
            );

    /* --------------------------------       채팅방       -------------------------------- */

    public static RedisChat REGISTER_SUGGEST_CHAT = AlarmMessageUtils
            .makeMemberAlarmMessage(
                    TUTOR_CHAT_ROOM_ID, TUTOR_ID, LESSON_TITLE, STUDENT_NAME, 0, null, AlarmType.REGISTER
            );

    public static RedisChat ACCEPT_SUGGEST_CHAT = AlarmMessageUtils
            .makeMemberAlarmMessage(
                    STUDENT_CHAT_ROOM_ID, STUDENT_ID, LESSON_TITLE, TUTOR_NAME, 0, null, AlarmType.ACCEPT
            );

    public static RedisChat REJECT_SUGGEST_CHAT = AlarmMessageUtils
            .makeMemberAlarmMessage(
                    STUDENT_CHAT_ROOM_ID, STUDENT_ID, LESSON_TITLE, TUTOR_NAME, 0, REJECT_MESSAGE, AlarmType.REJECT
            );

    public static RedisChat CANCEL_SUGGEST_CHAT = AlarmMessageUtils
            .makeMemberAlarmMessage(
                    TUTOR_CHAT_ROOM_ID, TUTOR_ID, LESSON_TITLE, STUDENT_NAME, 0, null, AlarmType.CANCEL
            );

    public static RedisChat PAY_SUCCESS_CHAT = AlarmMessageUtils
            .makeMemberAlarmMessage(
                    TUTOR_CHAT_ROOM_ID, TUTOR_ID, LESSON_TITLE, STUDENT_NAME, 0, null, AlarmType.PAYMENT_SUCCESS
            );

    public static RedisChat CHECK_ATTENDANCE_CHAT = AlarmMessageUtils
            .makeMemberAlarmMessage(
                    STUDENT_CHAT_ROOM_ID, STUDENT_ID, LESSON_TITLE, STUDENT_NAME, 3, null, AlarmType.CHECK_ATTENDANCE
            );

    public static RedisChat CANCEL_ATTENDANCE_CHAT = AlarmMessageUtils
            .makeMemberAlarmMessage(
                    STUDENT_CHAT_ROOM_ID, STUDENT_ID, LESSON_TITLE, STUDENT_NAME, 4, null, AlarmType.CANCEL_ATTENDANCE
            );

    public static RedisChat REFUND_REQUEST_CHAT = AlarmMessageUtils
            .makeMemberAlarmMessage(
                    TUTOR_CHAT_ROOM_ID, TUTOR_ID, LESSON_TITLE, STUDENT_NAME, 0, null, AlarmType.REFUND_REQUEST
            );

    public static RedisChat ACCEPT_REFUND_CHAT = AlarmMessageUtils
            .makeMemberAlarmMessage(
                    STUDENT_CHAT_ROOM_ID, STUDENT_ID, LESSON_TITLE, null, 4, null, AlarmType.ACCEPT_REFUND
            );

    public static RedisChat REJECT_REFUND_CHAT = AlarmMessageUtils
            .makeMemberAlarmMessage(
                    STUDENT_CHAT_ROOM_ID, STUDENT_ID, LESSON_TITLE, null, 0, REJECT_MESSAGE, AlarmType.REJECT_REFUND
            );

    public static RedisChat END_LESSON_CHAT = AlarmMessageUtils
            .makeMemberAlarmMessage(
                    STUDENT_CHAT_ROOM_ID, STUDENT_ID, LESSON_TITLE, TUTOR_NAME, 0, REJECT_MESSAGE, AlarmType.END
            );

    public static RedisChat COMPLETED_REVIEW_CHAT = AlarmMessageUtils
            .makeMemberAlarmMessage(
                    TUTOR_CHAT_ROOM_ID, TUTOR_ID, LESSON_TITLE, STUDENT_NAME, 0, REVIEW_MESSAGE, AlarmType.COMPLETED_REVIEW
            );

    public static ChatRoom CHAT_ROOM1 = ChatRoom.builder()
            .senderId(STUDENT_ID)
            .senderName(STUDENT_NAME)
            .receiverId(TUTOR_ID)
            .receiverName(TUTOR_NAME)
            .build();

    public static ChatRoom CHAT_ROOM2 = ChatRoom.builder()
            .senderId(TUTOR_ID)
            .senderName(TUTOR_NAME)
            .receiverId(STUDENT_ID)
            .receiverName(STUDENT_NAME)
            .build();

    public static ChatRoom TUTOR_CHAT_ROOM = ChatRoom.builder()
            .senderId(TUTOR_ID)
            .senderName(TUTOR_NAME)
            .receiverId(TUTOR_ID)
            .receiverName(TUTOR_NAME)
            .build();

    public static ChatRoom STUDENT_CHAT_ROOM = ChatRoom.builder()
            .senderId(STUDENT_ID)
            .senderName(STUDENT_NAME)
            .receiverId(STUDENT_ID)
            .receiverName(STUDENT_NAME)
            .build();

    public static ChatRoom getSavedChatRoom() {
        ChatRoom chatRoom = CHAT_ROOM1;
        Field field = org.springframework.util.ReflectionUtils.findField(chatRoom.getClass(), "id");
        ReflectionUtils.makeAccessible(field);
        ReflectionUtils.setField(field, chatRoom, CHAT_ROOM_ID1);
        return chatRoom;
    }

    public static ChatRoom getSavedTutorChatRoom() {
        ChatRoom chatRoom = TUTOR_CHAT_ROOM;
        Field field = org.springframework.util.ReflectionUtils.findField(chatRoom.getClass(), "id");
        ReflectionUtils.makeAccessible(field);
        ReflectionUtils.setField(field, chatRoom, TUTOR_CHAT_ROOM_ID);
        return chatRoom;
    }

    public static ChatRoom getSavedStudentChatRoom() {
        ChatRoom chatRoom = STUDENT_CHAT_ROOM;
        Field field = org.springframework.util.ReflectionUtils.findField(chatRoom.getClass(), "id");
        ReflectionUtils.makeAccessible(field);
        ReflectionUtils.setField(field, chatRoom, STUDENT_CHAT_ROOM_ID);
        return chatRoom;
    }
}
