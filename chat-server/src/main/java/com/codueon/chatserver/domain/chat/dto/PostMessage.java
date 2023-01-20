package com.codueon.chatserver.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class PostMessage {
    @NotBlank
    private Long chatRoomId;
    @NotBlank
    private String messageContent;

    @Builder
    public PostMessage(Long chatRoomId, String messageContent) {
        this.chatRoomId = chatRoomId;
        this.messageContent = messageContent;
    }
}
