package com.codueon.boostUp.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GetAlarmMessage {
    private Long chatRoomId;
    private Integer alarmCount;
    private String latestMessage;
    private LocalDateTime createdAt;

    @Builder
    public GetAlarmMessage(Long chatRoomId, Integer alarmCount, String latestMessage,
                           LocalDateTime createdAt) {
        this.chatRoomId = chatRoomId;
        this.alarmCount = alarmCount;
        this.latestMessage = latestMessage;
        this.createdAt = createdAt;
    }

    public static GetAlarmMessage of(RedisChat latestChat, int alarmCount) {
        return GetAlarmMessage.builder()
                .chatRoomId(latestChat.getChatRoomId())
                .alarmCount(alarmCount)
                .latestMessage(latestChat.getMessage())
                .createdAt(latestChat.getCreatedAt())
                .build();
    }
}
