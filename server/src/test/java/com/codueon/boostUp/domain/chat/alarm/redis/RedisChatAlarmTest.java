package com.codueon.boostUp.domain.chat.alarm.redis;

import com.codueon.boostUp.domain.IntegrationTest;
import com.codueon.boostUp.domain.chat.repository.redis.RedisChatAlarm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.codueon.boostUp.domain.chat.utils.DataForChat.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RedisChatAlarmTest extends IntegrationTest {
    @Autowired
    protected RedisChatAlarm redisChatAlarm;

    @AfterEach
    void afterEach() {
        redisChatAlarm.deleteAlarmCount(TUTOR_ID, CHAT_ROOM_ID1);
        redisChatAlarm.deleteAlarmCount(STUDENT_ID, CHAT_ROOM_ID1);
    }

    @Test
    @DisplayName("채팅방 생성 시 알람 메시지를 초기화한다. (Receiver)")
    void makeChatRoomAndEnterAlarmTest() {
        // given and when
        redisChatAlarm.makeChatRoomAndEnterAlarm(TUTOR_ID, CHAT_ROOM_ID1);

        // then
        assertThat(redisChatAlarm.getAlarmCount(TUTOR_ID, CHAT_ROOM_ID1)).isEqualTo(1);
    }

    @Test
    @DisplayName("알람 카운트를 저장한다.")
    void saveAlarmCount() {
        // given and when
        redisChatAlarm.saveAlarmCount(TUTOR_ID, CHAT_ROOM_ID1, 3);

        // then
        assertThat(redisChatAlarm.getAlarmCount(TUTOR_ID, CHAT_ROOM_ID1)).isEqualTo(3);
    }

    @Test
    @DisplayName("메시지 전송 시 Receiver의 알람을 1 증가시켜야 한다.")
    void increaseCharRoomAlarmTest() {
        // given
        redisChatAlarm.saveAlarmCount(TUTOR_ID, CHAT_ROOM_ID1, 0);

        // when
        redisChatAlarm.increaseCharRoomAlarm(TUTOR_ID, CHAT_ROOM_ID1);

        // then
        assertThat(redisChatAlarm.getAlarmCount(TUTOR_ID, CHAT_ROOM_ID1)).isEqualTo(1);
    }

    @Test
    @DisplayName("알람 카운트가 존재하는 경우 알람 카운트를 반환한다.")
    void getAlarmCountExistAlarmCountCaseTest() {
        // given
        redisChatAlarm.saveAlarmCount(TUTOR_ID, CHAT_ROOM_ID1, 4);

        // when and then
        assertThat(redisChatAlarm.getAlarmCount(TUTOR_ID, CHAT_ROOM_ID1)).isEqualTo(4);
    }

    @Test
    @DisplayName("알람 카운트가 존재하지 않을 경우 0 값의 알람 카운트를 저장하고 0을 반환한다.")
    void getAlarmCountNotExistAlarmCountCaseTest() {
        // when and then
        assertThat(redisChatAlarm.getAlarmCount(TUTOR_ID, CHAT_ROOM_ID1)).isEqualTo(0);
    }

    @Test
    @DisplayName("알람 카운트를 제거한다.")
    void deleteAlarmCountTest() {
        // given
        redisChatAlarm.saveAlarmCount(TUTOR_ID, CHAT_ROOM_ID1, 4);

        // when
        redisChatAlarm.deleteAlarmCount(TUTOR_ID, CHAT_ROOM_ID1);

        // then
        assertThat(redisChatAlarm.getAlarmCount(TUTOR_ID, CHAT_ROOM_ID1)).isEqualTo(0);
    }

    @Test
    @DisplayName("알람 카운트 키를 생성한다.")
    void getKeyTest() {
        // given and when and then
        assertThat(redisChatAlarm.getKey(TUTOR_ID, CHAT_ROOM_ID1))
                .isEqualTo("Member" + TUTOR_ID + "ChatRoom" + CHAT_ROOM_ID1 + "Alarm");
    }
}
