package com.codueon.boostUp.domain.chat.dto;

import com.codueon.boostUp.domain.chat.utils.MessageType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class PostMessage {

    @NotNull
    private Long senderId;
    @NotNull
    private Long receiverId;
    @NotBlank
    private Long chatRoomId;
    @NotBlank
    private String messageContent;

    @Builder
    public PostMessage(Long senderId,
                       Long receiverId,
                       Long chatRoomId,
                       String messageContent) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.chatRoomId = chatRoomId;
        this.messageContent = messageContent;
    }
}
