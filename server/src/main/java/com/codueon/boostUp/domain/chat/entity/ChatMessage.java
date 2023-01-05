package com.codueon.boostUp.domain.chat.entity;

import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.global.util.Auditable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage {

    @Id
    @Column(name = "CHAT_MESSAGE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_chat_to_sender"))
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_room_to_receiver"))
    private Member receiver;

    @Column(length = 5000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room")
    private ChatRoom room;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Builder
    public ChatMessage(Long id,
                       Member sender,
                       Member receiver,
                       String content,
                       ChatRoom room,
                       LocalDateTime createdAt) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.room = room;
        this.createdAt = createdAt;
    }
}
