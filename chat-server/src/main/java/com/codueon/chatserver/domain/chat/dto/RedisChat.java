package com.codueon.chatserver.domain.chat.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RedisChat implements Serializable {

    @NotNull
    private Long chatRoomId;
    @NotNull
    private Long receiverId;

    @NotNull
    private String message;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;

    @Builder
    public RedisChat(Long chatRoomId,
                     Long receiverId,
                     String message,
                     LocalDateTime createdAt) {
        this.chatRoomId = chatRoomId;
        this.receiverId = receiverId;
        this.message = message;
        this.createdAt = createdAt;
    }
}
