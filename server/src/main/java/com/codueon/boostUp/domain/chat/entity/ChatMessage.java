package com.codueon.boostUp.domain.chat.entity;

import com.codueon.boostUp.domain.chat.utils.MessageType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage implements Serializable {

    @Id
    @Column(name = "CHAT_MESSAGE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Long chatRoomId;
    @NotNull
    private Long senderId;
    @NotNull
    private String message;
    private String displayName;

    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;

    @Builder
    public ChatMessage(Long chatRoomId, Long senderId, String message,
                       String displayName, MessageType messageType, LocalDateTime createdAt) {
        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
        this.message = message;
        this.displayName = displayName;
        this.messageType = messageType;
        this.createdAt = createdAt;
    }
}
