package com.codueon.boostUp.domain.chat.entity;

import lombok.AccessLevel;
import lombok.Builder;
import javax.persistence.*;
import java.io.Serializable;
import com.codueon.boostUp.global.utils.Auditable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends Auditable implements Serializable {

    @Id
    @Column(name = "CHAT_ROOM_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long senderId;
    private Long receiverId;

    @Builder
    public ChatRoom(Long id,
                    Long senderId,
                    Long receiverId) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }
}
