package com.codueon.boostUp.domain.chat.utils;

import com.codueon.boostUp.domain.chat.dto.GetAlarmMessage;
import com.codueon.boostUp.domain.chat.dto.GetChatRoom;
import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.global.exception.BusinessLogicException;
import com.codueon.boostUp.global.exception.ExceptionCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisSubscriber {
    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate operations;

    /**
     * 채팅방 메시지 전송용 Listener 메서드
     * @param publishedMessage 전송할 메시지(JSON) - RedisChat
     * @author mozzi327
     */
    public void sendMessage(String publishedMessage) {
        try {
            RedisChat redisChat = objectMapper.readValue(publishedMessage, RedisChat.class);
            log.info("[LISTENER] {}", redisChat.getChatRoomId());
            operations.convertAndSend("/topic/rooms/" + redisChat.getChatRoomId(), redisChat);
            log.info("[COMPLETE SEND]");
        } catch (JsonProcessingException e) {
            throw new BusinessLogicException(ExceptionCode.FAIL_TO_SERIALIZE);
        }
    }

    /**
     * 채팅방 목록 알람 전송용 Listener 메서드
     * @param publishedMessage 전송할 알람(JSON) - GetAlarmMessage
     * @author mozzi327
     */
    public void sendAlarm(String publishedMessage) {
        try {
            GetAlarmMessage alarmMessage = objectMapper.readValue(publishedMessage, GetAlarmMessage.class);
            operations.convertAndSend("/topic/member/" + alarmMessage.getReceiverId(), alarmMessage);
        } catch (JsonProcessingException e) {
            throw new BusinessLogicException(ExceptionCode.FAIL_TO_SERIALIZE);
        }
    }

    /**
     * 채팅방 목록에 입장 시 알람을 전송하기 위한 메서드
     * @param publishedMessage 전송할 알람(JSON) - GetChatRoom
     * @author mozzi327
     */
    public void sendEnterAlarm(String publishedMessage) {
        try {
            GetChatRoom firstAlarm = objectMapper.readValue(publishedMessage, GetChatRoom.class);
            operations.convertAndSend("/topic/member/" + firstAlarm.getReceiverId(), firstAlarm);
        } catch (JsonProcessingException e) {
            throw new BusinessLogicException(ExceptionCode.FAIL_TO_SERIALIZE);
        }
    }
}
