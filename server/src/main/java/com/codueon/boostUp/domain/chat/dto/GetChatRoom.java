package com.codueon.boostUp.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GetChatRoom implements Comparable<GetChatRoom> {
    private Long chatRoomId;
    private Long receiverId;
    private int alarmCount;
    private String displayName;
    private String latestMessage;
    private LocalDateTime createdAt;

    @Builder
    public GetChatRoom(Long chatRoomId,
                       Long receiverId,
                       int alarmCount,
                       String displayName,
                       RedisChat redisChat) {
        this.chatRoomId = chatRoomId;
        this.receiverId = receiverId;
        this.alarmCount = alarmCount;
        this.displayName = displayName;
        this.latestMessage = redisChat.getMessage();
        this.createdAt = redisChat.getCreatedAt();
    }

    @Override
    public int compareTo(GetChatRoom o) {
        return o.getCreatedAt().compareTo(this.getCreatedAt());
    }
}
