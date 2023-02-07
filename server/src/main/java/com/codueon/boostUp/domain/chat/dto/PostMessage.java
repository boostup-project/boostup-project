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
    private Long memberId;
    @NotBlank
    private Long chatRoomId;
    @NotBlank
    private String memberImage;
    @NotBlank
    private String messageContent;

    @Builder
    public PostMessage(Long memberId,
                       Long chatRoomId,
                       String memberImage,
                       String messageContent) {
        this.memberId = memberId;
        this.chatRoomId = chatRoomId;
        this.memberImage = memberImage;
        this.messageContent = messageContent;
    }
}
