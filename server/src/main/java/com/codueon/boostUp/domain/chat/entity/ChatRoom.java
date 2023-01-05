package com.codueon.boostUp.domain.chat.entity;

import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.global.util.Auditable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends Auditable implements Serializable {

    @Id
    @Column(name = "CHAT_ROOM_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_room_to_merchant"))
    private Member merchant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_room_to_student"))
    private Member student;

    @Builder
    public ChatRoom(Long id, Member merchant, Member student) {
        this.id = id;
        this.merchant = merchant;
        this.student = student;
    }

    public Member getPartner(Long memberId) {
        if (student.getId() == memberId) return merchant;
        return student;
    }
}
