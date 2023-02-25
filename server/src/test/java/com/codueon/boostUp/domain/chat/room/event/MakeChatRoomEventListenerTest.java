package com.codueon.boostUp.domain.chat.room.event;

import com.codueon.boostUp.domain.chat.ChatTest;
import com.codueon.boostUp.domain.chat.dto.GetChatRoom;
import com.codueon.boostUp.domain.chat.event.listener.MakeChatRoomEventListener;
import com.codueon.boostUp.domain.chat.event.vo.MakeAlarmChatRoomEvent;
import com.codueon.boostUp.domain.chat.event.vo.MakeChatRoomEvent;
import com.codueon.boostUp.domain.lesson.entity.Lesson;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.utils.DataForTest;
import com.codueon.boostUp.domain.vo.AuthInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MakeChatRoomEventListenerTest extends ChatTest {
    protected MakeChatRoomEventListener makeChatRoomEventListener;

    @BeforeEach
    void setUp() throws Exception {
        makeChatRoomEventListener = new MakeChatRoomEventListener(chatRoomService);
    }

    @AfterEach
    void afterAll() throws Exception {
        chatService.deleteAllNewChatMessage();
        chatService.deleteChatMessage(1L);
        chatService.deleteChatMessage(2L);
        chatService.deleteChatMessage(3L);
        chatAlarmService.initAlarm(1L, 1L);
        chatAlarmService.initAlarm(2L, 1L);
        chatAlarmService.initAlarm(1L, 2L);
        chatAlarmService.initAlarm(2L, 2L);
        chatRoomService.deleteRedisChatRoomKey(1L);
        chatRoomService.deleteRedisChatRoomKey(2L);
    }

    @Test
    @Sql("classpath:sql/initChatTest.sql")
    @DisplayName("회원가입 시 채팅방 알림톡 및 환영 메시지가 생성되어야 한다.")
    void sendJoinMemberMessageTest() throws Exception {
        // given
        Member tutor = memberRepository.save(DataForTest.getTutor());

        MakeAlarmChatRoomEvent event = MakeAlarmChatRoomEvent.builder()
                .memberId(tutor.getId())
                .memberNickname(tutor.getName())
                .build();

        // when
        makeChatRoomEventListener.handleSendJoinMemberMessage(event);

        // then
        GetChatRoom savedChatRoom = chatRoomService.findAllChatRoom(tutor.getId()).get(0);
        assertThat(savedChatRoom.getReceiverId()).isEqualTo(tutor.getId());
        assertThat(savedChatRoom.getDisplayName()).isEqualTo("코듀온 알리미");
    }

    @Test
    @Sql("classpath:sql/initChatTest.sql")
    @DisplayName("과외 신청 시 유저 간 채팅방이 생성되어야 한다.")
    public void makeChatRoomWhenRegisterSuggestTest() throws Exception {
        // given
        Lesson savedLesson = lessonRepository.save(DataForTest.getSaveLesson());
        suggestRepository.save(DataForTest.getSaveSuggest());
        Member tutor = memberRepository.save(DataForTest.getTutor());
        Member student = memberRepository.save(DataForTest.getStudent());

        MakeChatRoomEvent event = MakeChatRoomEvent.builder()
                .authInfo(AuthInfo.builder()
                        .memberId(student.getId())
                        .name(student.getName())
                        .build())
                .lessonId(savedLesson.getId())
                .build();

        // when
        makeChatRoomEventListener.handleMakeChatRoomWhenRegisterSuggest(event);

        // then
        GetChatRoom savedChatRoom = chatRoomService.findAllChatRoom(student.getId()).get(0);
        assertThat(savedChatRoom.getReceiverId()).isEqualTo(tutor.getId());
        assertThat(savedChatRoom.getDisplayName()).isEqualTo("선생이에요");
    }
}
