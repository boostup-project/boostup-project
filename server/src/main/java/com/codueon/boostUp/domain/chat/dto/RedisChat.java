package com.codueon.boostUp.domain.chat.dto;

import com.codueon.boostUp.domain.chat.utils.MessageType;
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
    private Long senderId;
    @NotNull
    private String message;
    private MessageType messageType;
    private String displayName;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;

    @Builder
    public RedisChat(Long chatRoomId,
                     Long senderId,
                     String message,
                     MessageType messageType,
                     String displayName) {
        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
        this.message = message;
        this.messageType = messageType;
        this.displayName = displayName;
    }

    public void setEnterMessage() {
        this.message = "[알림] " + displayName + "님이 입장하셨습니다.";
    }

    public void setLeaveMessage() {
        this.message = "[알림] " + displayName + "님이 나가셨습니다.";
    }

    public void setCurrentTime() {
        this.createdAt = LocalDateTime.now();
    }
}
