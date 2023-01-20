package com.codueon.chatserver.domain.chat.entity;

import com.codueon.chatserver.domain.member.entity.Member;
import com.codueon.chatserver.global.util.Auditable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends Auditable implements Serializable {

    @Id
    @Column(name = "CHAT_ROOM_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_room_to_sender"))
    private Member sender;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_room_to_receiver"))
    private Member receiver;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> chatMessages = new ArrayList<>();

    @Builder
    public ChatRoom(Long id, Member tutor, Member student) {
        this.id = id;
        this.sender = tutor;
        this.receiver = student;
    }

    public void addChatMessage(ChatMessage chatMessage) {
        if (chatMessage.getChatRoom() != this) chatMessage.addChatRoom(this);
        chatMessages.add(chatMessage);
    }

    public Member getPartner(Long memberId) {
        if (receiver.getId() == memberId) return sender;
        return receiver;
    }
}
