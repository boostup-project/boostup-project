package com.codueon.boostUp.domain.chat.dto.res;

import com.codueon.boostUp.domain.chat.entity.ChatMessage;
import com.codueon.boostUp.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GetLastChat {
    private Long roomId;
    private Long merchantId;
    private Long studentId;
    private String name;
    private String latestMessage;
    private LocalDateTime createdAt;
    private String profileImage;

    @Builder
    public GetLastChat(Long roomId,
                       Long merchantId,
                       Long studentId,
                       String name,
                       String latestMessage,
                       LocalDateTime createdAt,
                       String profileImage) {
        this.roomId = roomId;
        this.merchantId = merchantId;
        this.studentId = studentId;
        this.name = name;
        this.latestMessage = latestMessage;
        this.createdAt = createdAt;
        this.profileImage = profileImage;
    }

    public static GetLastChat of(Member partner, ChatMessage latestChat) {
        return GetLastChat.builder()
                .roomId(latestChat.getRoom().getId())
                .merchantId(latestChat.getRoom().getMerchant().getId())
                .studentId(latestChat.getRoom().getStudent().getId())
                .name(partner.getName())
                .profileImage(partner.getMemberImage().getFilePath())
                .latestMessage(latestChat.getContent())
                .createdAt(latestChat.getCreatedAt())
                .build();
    }
}
