package com.codueon.boostUp.domain.chat.dto;

import com.codueon.boostUp.domain.chat.utils.AlarmType;
import com.codueon.boostUp.domain.chat.utils.MessageType;
import com.codueon.boostUp.domain.vo.AuthVO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RedisChat implements Serializable {
    @NotNull
    private Long chatRoomId;
    @NotNull
    private Long senderId;
    @NotNull
    private String message;
    private String displayName;
    private MessageType messageType;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;

    @Builder
    @QueryProjection
    public RedisChat(Long chatRoomId,
                     Long senderId,
                     String message,
                     MessageType messageType,
                     String displayName,
                     LocalDateTime createdAt) {
        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
        this.message = message;
        this.messageType = messageType;
        this.displayName = displayName;
        this.createdAt = createdAt;
    }

    public static RedisChat of(PostMessage message, AuthVO authInfo) {
        return RedisChat.builder()
                .chatRoomId(message.getChatRoomId())
                .senderId(authInfo.getMemberId())
                .displayName(authInfo.getName())
                .message(message.getMessageContent())
                .messageType(MessageType.TALK)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
