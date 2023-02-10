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

    public void addEnterMessage() {
        this.message = "[알림] " + displayName + "님이 입장하셨습니다.";
    }

    public void addLeaveMessage() {
        this.message = "[알림] " + displayName + "님이 나가셨습니다.";
    }

    public void addRegisterSuggestMessage() {
        this.message = "[알림] " + displayName + "님께서 과외를 신청했습니다.";
    }

    public void addRejectSuggestMessage(String rejectComment) {
        this.message = "[알림] " + displayName + "님께서 과외 신청을 거절하셨어요! \n"
                + "거절 사유 : \n"
                + rejectComment;
    }

    public void addAcceptSuggestMessage() {
        this.message = "[알림] " + displayName + "님 과외가 수락되었습니다.";
    }

    public void settingCurrentTime() {
        this.createdAt = LocalDateTime.now();
    }
}
