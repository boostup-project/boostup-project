package com.codueon.boostUp.domain.chat.entity;

import com.codueon.boostUp.domain.member.entity.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberInfoInChatRoom {
    @Setter
    private Long chatRoomId;
    private Member member;

    public void setMember(MemberInChatRoom member) {
        if (member == null) return;
        this.member = Member.builder()
                .name(member.getDisplayName())
                .email(member.getEmail())
                .build();
    }
}
