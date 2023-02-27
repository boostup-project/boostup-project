package com.codueon.boostUp.domain.chat.room.service;

import com.codueon.boostUp.domain.IntegrationTest;
import com.codueon.boostUp.domain.chat.entity.ChatRoom;
import com.codueon.boostUp.domain.chat.repository.querydsl.ChatRoomRepository;
import com.codueon.boostUp.domain.chat.repository.redis.RedisChatAlarm;
import com.codueon.boostUp.domain.chat.repository.redis.RedisChatMessage;
import com.codueon.boostUp.domain.chat.repository.redis.RedisChatRoom;
import com.codueon.boostUp.domain.chat.service.ChatRoomService;
import com.codueon.boostUp.domain.member.service.MemberDbService;
import com.codueon.boostUp.domain.utils.DataForTest;
import com.codueon.boostUp.global.exception.BusinessLogicException;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChatRoomServiceTest extends IntegrationTest {
    @Autowired
    protected ChatRoomService chatRoomService;
    @Autowired
    protected RedisChatRoom redisChatRoom;
    @Autowired
    protected RedisChatAlarm redisChatAlarm;
    @Autowired
    protected RedisChatMessage redisChatMessage;
    @MockBean
    protected MemberDbService memberDbService;
    @MockBean
    protected ChatRoomRepository chatRoomRepository;

    protected Long tutorId, studentId;
    protected String tutorName, studentName;

    @BeforeAll
    void setUp() {
        tutorId = 1L;
        studentId = 2L;
        tutorName = "선생이에요";
        studentName = "학생이에요";
    }

    @AfterEach
    void afterEach() {
        redisChatMessage.deleteAllNewChat();
        redisChatMessage.deleteAllMessageInChatRoom(1L);
        redisChatAlarm.deleteAlarmCount(1L, 1L);
        redisChatAlarm.deleteAlarmCount(2L, 1L);
        redisChatRoom.deleteAllChatRoomKey(1L);
        redisChatRoom.deleteAllChatRoomKey(2L);
    }

    @Test
    @DisplayName("알람 채팅방 생성 테스트 (존재할 경우)")
    void createAlarmChatRoomExistCaseTest() {
        // given
        given(chatRoomRepository.existsBySenderIdAndReceiverId(1L, 1L)).willReturn(true);

        // when
        assertThatThrownBy
                (() -> chatRoomService.createAlarmChatRoom(tutorId, tutorName))
                .isInstanceOf(BusinessLogicException.class);
    }

    @Test
    @DisplayName("알람 채팅방 생성 테스트 (존재하지 않을 경우)")
    void createAlarmChatRoomNotExistCaseTest() {
        // given
        given(chatRoomRepository.existsBySenderIdAndReceiverId(1L, 1L)).willReturn(false);
        given(chatRoomRepository.save(Mockito.any(ChatRoom.class))).willReturn(DataForTest.getSavedChatRoom());

        // when
        assertThatCode
                (() -> chatRoomService.createAlarmChatRoom(tutorId, tutorName))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Redis 채팅방 식별 키 전체 조회 테스트 (문자열)")
    void findAllChatRoomKeyTest() {
        // given
        redisChatRoom.createChatRoom(1L, studentId);
        redisChatRoom.createChatRoom(2L, studentId);

        // when
        List<String> keys = chatRoomService.findAllChatRoomKey(studentId);

        // then
        assertThat(keys.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Redis 채팅방 식별 키 전체 조회 테스트 (Long)")
    void findAllChatRoomKeyAsLongTest() {
        // given
        redisChatRoom.createChatRoom(1L, studentId);
        redisChatRoom.createChatRoom(2L, studentId);

        // when
        List<Long> keys = chatRoomService.findAllChatRoomKeyAsLong(studentId);

        // then
        assertThat(keys.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("알람 채팅방 존재 유무 확인 및 리턴 테스트 (존재할 경우)")
    void checkAlarmChatRoomExistCaseTest() {
        // given
        given(chatRoomRepository.findBySenderIdAndReceiverId(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(Optional.of(ChatRoom.builder().build()));

        // when and then
        assertThatCode
                (() -> chatRoomService.ifExistsAlarmChatRoomThenReturn(1L))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("알람 채팅방 존재 유무 확인 및 리턴 테스트 (존재하지 않을 경우)")
    void checkAlarmChatRoomNotExistCaseTest() {
        // given
        given(chatRoomRepository.findBySenderIdAndReceiverId(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(Optional.empty());

        // when and then
        assertThatThrownBy
                (() -> chatRoomService.ifExistsAlarmChatRoomThenReturn(1L))
                .isInstanceOf(BusinessLogicException.class);
    }

    @Test
    @DisplayName("Redis 채팅방 식별 키 삭제 테스트")
    void leaveChatRoomTest() {
        // given
        redisChatRoom.createChatRoom(1L, studentId);
        redisChatRoom.createChatRoom(2L, studentId);
        doNothing().when(chatRoomRepository).deleteById(Mockito.anyLong());

        // when
        chatRoomService.leaveChatRoom(1L, studentId);

        // then
        List<Long> keys = chatRoomService.findAllChatRoomKeyAsLong(studentId);
        assertThat(keys.size()).isEqualTo(1);
        assertThat(keys.get(0)).isEqualTo(2L);
    }

    @Test
    @DisplayName("Redis 채팅방 식별 키 전체 삭제 테스트")
    void deleteRedisChatRoomKeyTest() {
        // given
        redisChatRoom.createChatRoom(1L, studentId);
        redisChatRoom.createChatRoom(2L, studentId);
        doNothing().when(chatRoomRepository).deleteAllBySenderIdOrReceiverId(Mockito.anyLong(), Mockito.anyLong());

        // when
        chatRoomService.deleteRedisChatRoomKey(studentId);

        // then
        List<String> keys = chatRoomService.findAllChatRoomKey(studentId);
        assertThat(keys.size()).isEqualTo(0);
    }
}
