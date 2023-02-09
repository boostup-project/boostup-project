package com.codueon.boostUp.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GetAlarmMessage {
    private Long chatRoomId;
    private Long receiverId;
    private Integer alarmCount;
    private String latestMessage;
    private LocalDateTime createdAt;

    @Builder
    public GetAlarmMessage(Long chatRoomId, Long receiverId, Integer alarmCount,
                           String latestMessage, LocalDateTime createdAt) {
        this.chatRoomId = chatRoomId;
        this.receiverId = receiverId;
        this.alarmCount = alarmCount;
        this.latestMessage = latestMessage;
        this.createdAt = createdAt;
    }

    public static GetAlarmMessage of(Long receiverId, RedisChat latestChat, int alarmCount) {
        return GetAlarmMessage.builder()
                .chatRoomId(latestChat.getChatRoomId())
                .alarmCount(alarmCount)
                .receiverId(receiverId)
                .latestMessage(latestChat.getMessage())
                .createdAt(latestChat.getCreatedAt())
                .build();
    }
}
