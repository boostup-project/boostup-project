package com.codueon.boostUp.domain.chat.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GetAlarmMessage implements Serializable {
    private Long chatRoomId;
    private Long receiverId;
    private Integer alarmCount;
    private String latestMessage;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
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
