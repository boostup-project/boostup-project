package com.codueon.boostUp.domain.chat.entity;

import lombok.AccessLevel;
import lombok.Builder;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import com.codueon.boostUp.global.utils.Auditable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends Auditable {

    @Id
    @Column(name = "CHAT_ROOM_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long senderId;
    private Long receiverId;
    private String senderName;
    private String receiverName;

    @Builder
    public ChatRoom(Long senderId,
                    String senderName,
                    Long receiverId,
                    String receiverName) {
        this.senderId = senderId;
        this.senderName = senderName;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
    }

    public String returnChatRoomName(Long memberId) {
        return (Objects.equals(memberId, senderId)) ? receiverName : senderName;
    }

    public Long returnReceiverId(Long memberId) {
        return (Objects.equals(memberId, senderId)) ? receiverId : senderId;
    }
}
