package com.codueon.boostUp.domain.chat.service;

import com.codueon.boostUp.domain.chat.dto.GetAlarmMessage;
import com.codueon.boostUp.domain.chat.dto.GetChatRoom;
import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.entity.ChatRoom;
import com.codueon.boostUp.domain.chat.repository.redis.RedisChatAlarm;
import com.codueon.boostUp.domain.chat.repository.redis.RedisChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendAlarmService {
    private final RedisChatAlarm redisChatAlarm;
    private final RedisChatMessage redisChatMessage;
    private final RedisTemplate<Object, Object> redisTemplate;

    /**
     * 알람 메시지 세팅 메서드
     * @param senderId 사용자 식별자(Sender)
     * @param receiverId 사용자 식별자(Receiver)
     * @param chatRoomId 채팅방 식별자
     * @author mozzi327
     */
    @Transactional
    public void setAlarmAndSendAlarm(Long senderId, Long receiverId, Long chatRoomId) {
        redisChatAlarm.increaseCharRoomAlarm(receiverId, chatRoomId);
        RedisChat latestChat = redisChatMessage.getLatestMessage(getKey(chatRoomId));
        int receiverAlarmCount = redisChatAlarm.getAlarmCount(receiverId, chatRoomId);
        GetAlarmMessage senderAlarm = GetAlarmMessage.of(senderId, latestChat, 0);
        GetAlarmMessage receiverAlarm = GetAlarmMessage.of(receiverId, latestChat, receiverAlarmCount);

        sendAlarm(senderAlarm, receiverAlarm, senderId, receiverId);
    }

    /**
     * 알람 메시지 전송 메서드(양쪽 채팅방 최신화)
     * @param senderAlarm 알람(Sender)
     * @param receiverAlarm 알람(Receiver)
     * @param senderId 사용자 식별자(Sender)
     * @param receiverId 사용자 식별자(Receiver)
     * @author mozzi327
     */
    private void sendAlarm(GetAlarmMessage senderAlarm, GetAlarmMessage receiverAlarm,
                          Long senderId, Long receiverId) {
        log.info("[SEND ALARM] START senderId : {}, receiverId : {}", senderId, receiverId);
        log.info("senderAlarm : {}", senderAlarm.getReceiverId());
        log.info("receiverAlarm : {}", receiverAlarm.getReceiverId());
        redisTemplate.convertAndSend("alarm", senderAlarm);
        redisTemplate.convertAndSend("alarm", receiverAlarm);
        log.info("[SEND ALARM] END");
    }

    /**
     * 입장 메시지 알람 전송 메서드 (메시지 받는 쪽만)
     *
     * @param chatRoom 채팅방 정보
     * @param receiverChat 메시지 정보(Receiver)
     * @author mozzi327
     */
    public void sendEnterAlarm(ChatRoom chatRoom, RedisChat receiverChat) {
        String displayName = chatRoom.returnChatRoomName(receiverChat.getSenderId());
        Long receiverId = receiverChat.getSenderId();
        int alarmCount = redisChatAlarm.makeChatRoomAndEnterAlarm(receiverId, chatRoom.getId());
        GetChatRoom makeEnterRoomAlarm = GetChatRoom.builder()
                .chatRoomId(chatRoom.getId())
                .alarmCount(alarmCount)
                .redisChat(receiverChat)
                .receiverId(receiverChat.getSenderId())
                .displayName(displayName)
                .build();
        redisTemplate.convertAndSend("firstAlarm", makeEnterRoomAlarm);
    }

    /**
     * Redis 채팅방 식별키 생성 메서드
     * @param chatRoomId 채팅방 식별자
     * @return String(Key)
     * @author mozzi327
     */
    private String getKey(Long chatRoomId) {
        return "ChatRoom" + chatRoomId + "Message";
    }
}
