package com.codueon.boostUp.domain.chat.room.event;

import com.codueon.boostUp.domain.chat.dto.GetChatRoom;
import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.event.listener.MakeChatRoomEventListener;
import com.codueon.boostUp.domain.chat.event.vo.MakeAlarmChatRoomEvent;
import com.codueon.boostUp.domain.chat.event.vo.MakeChatRoomEvent;
import com.codueon.boostUp.domain.chat.ChatRoomTest;
import com.codueon.boostUp.domain.chat.utils.MessageType;
import com.codueon.boostUp.domain.lesson.repository.LessonRepository;
import com.codueon.boostUp.domain.suggest.repository.SuggestRepository;
import com.codueon.boostUp.domain.utils.DataForTest;
import com.codueon.boostUp.domain.vo.AuthVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MakeChatRoomEventListenerTest extends ChatRoomTest {
    @Autowired
    protected LessonRepository lessonRepository;
    @Autowired
    protected SuggestRepository suggestRepository;
    protected MakeChatRoomEventListener makeChatRoomEventListener;

    @BeforeEach
    void setUp() {
        makeChatRoomEventListener = new MakeChatRoomEventListener(chatRoomService);
    }

    @Test
    @DisplayName("회원가입 시 채팅방 알림톡 및 환영 메시지가 생성되어야 한다.")
    void sendJoinMemberMessageTest() throws Exception {
        // given
        MakeAlarmChatRoomEvent event = MakeAlarmChatRoomEvent.builder()
                .memberId(1L)
                .memberNickname("선생이에요")
                .build();

        // when
        makeChatRoomEventListener.handleSendJoinMemberMessage(event);

        // then
        GetChatRoom savedChatRoom = chatRoomService.findAllChatRoom(1L).get(0);
        assertThat(savedChatRoom.getChatRoomId() == 1L);
        assertThat(savedChatRoom.getReceiverId() == 1L);
        assertThat(savedChatRoom.getDisplayName().equals("코듀온 알리미"));

        RedisChat savedAlarmMessage = chatService.getChatMessages(1L, 1L).get(0);
        assertThat(savedAlarmMessage.getChatRoomId() == 1L);
        assertThat(savedAlarmMessage.getSenderId() == 1L);
        assertThat(savedAlarmMessage.getDisplayName().equals("코듀온 알리미"));
        assertThat(savedAlarmMessage.getMessage().equals(
                "[알림] 선생이에요님! 코듀온에 가입하신 것을 진심으로 환영합니다! ^0^\n" +
                "지금 과외 선생님을 만나보세요!\n\n" +
                        "- 과외 신청하러가기 : https://codueon.com\n" +
                        "- 과외 등록하러가기 : https://codueon.com\n"));
    }

    @Test
    @DisplayName("과외 신청 시 유저 간 채팅방이 생성되어야 한다.")
    public void makeChatRoomWhenRegisterSuggestTest() throws Exception {
        // given
        lessonRepository.save(DataForTest.getSaveLesson());
        suggestRepository.save(DataForTest.getSaveSuggest());
        memberRepository.saveAll(List.of(DataForTest.getTutor(), DataForTest.getStudent()));

        MakeChatRoomEvent event = MakeChatRoomEvent.builder()
                .authInfo(AuthVO.builder()
                        .memberId(2L)
                        .name("학생이에요")
                        .build())
                .lessonId(1L)
                .build();

        // when
        makeChatRoomEventListener.handleMakeChatRoomWhenRegisterSuggest(event);

        // then
        GetChatRoom savedChatRoom = chatRoomService.findAllChatRoom(2L).get(0);
        assertThat(savedChatRoom.getChatRoomId() == 3L);
        assertThat(savedChatRoom.getReceiverId() == 1L);
        assertThat(savedChatRoom.getDisplayName().equals("선생이에요"));

        RedisChat firstChatMessage = chatService.getChatMessages(1L, 1L).get(0);
        RedisChat secondChatMessage = chatService.getChatMessages(1L, 1L).get(1);

        assertThat(firstChatMessage.getChatRoomId() == 1L);
        assertThat(firstChatMessage.getSenderId() == 2L);
        assertThat(firstChatMessage.getMessage().equals("[알림] 학생이에요님이 입장하셨습니다."));
        assertThat(firstChatMessage.getDisplayName().equals("코듀온 알리미"));
        assertThat(firstChatMessage.getMessageType().equals(MessageType.ALARM));

        assertThat(secondChatMessage.getChatRoomId() == 1L);
        assertThat(secondChatMessage.getSenderId() == 1L);
        assertThat(secondChatMessage.getMessage().equals("[알림] 선생이에요님이 입장하셨습니다."));
        assertThat(firstChatMessage.getDisplayName().equals("코듀온 알리미"));
        assertThat(firstChatMessage.getMessageType().equals(MessageType.ALARM));
    }
}
