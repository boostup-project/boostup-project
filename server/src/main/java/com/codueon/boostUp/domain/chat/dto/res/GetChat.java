package com.codueon.boostUp.domain.chat.dto.res;

import com.codueon.boostUp.domain.chat.entity.ChatMessage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GetChat {
    private Long memberId;
    private String profileImage;
    private String name;
    private String content;
    private LocalDateTime createdAt;

    @Builder
    public GetChat(Long memberId,
                   String profileImage,
                   String name,
                   String content,
                   LocalDateTime createdAt) {
        this.memberId = memberId;
        this.profileImage = profileImage;
        this.name = name;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static GetChat of(ChatMessage chatMessage) {
        return GetChat.builder()
                .memberId(chatMessage.getSender().getId())
                .profileImage(chatMessage.getSender().getMemberImage().getFilePath())
                .name(chatMessage.getSender().getName())
                .content(chatMessage.getContent())
                .createdAt(chatMessage.getCreatedAt())
                .build();
    }
}
