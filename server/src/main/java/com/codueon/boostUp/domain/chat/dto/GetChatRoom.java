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
public class GetChatRoom implements Comparable<GetChatRoom>, Serializable {
    private Long chatRoomId;
    private Long receiverId;
    private int alarmCount;
    private String displayName;
    private String latestMessage;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;

    @Builder
    public GetChatRoom(Long chatRoomId,
                       Long receiverId,
                       int alarmCount,
                       String displayName,
                       String latestMessage,
                       LocalDateTime createdAt) {
        this.chatRoomId = chatRoomId;
        this.receiverId = receiverId;
        this.alarmCount = alarmCount;
        this.displayName = displayName;
        this.latestMessage = latestMessage;
        this.createdAt = createdAt;
    }

    @Override
    public int compareTo(GetChatRoom o) {
        return o.getCreatedAt().compareTo(this.getCreatedAt());
    }
}
