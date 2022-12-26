package com.codueon.boostUp.domain.chat.dto.res;

import com.codueon.boostUp.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GetChatRoom {
    private List<GetChatMember> members;
    List<GetChat> chatResponses;

    @Builder
    public GetChatRoom(Member memberA, Member memberB, List<GetChat> chatResponses) {
        this.members = List.of(
                GetChatMember.builder()
                        .memberId(memberA.getId())
                        .profileImage(memberA.getMemberImage().getFilePath())
                        .name(memberA.getName())
                        .build(),
                GetChatMember.builder()
                        .memberId(memberB.getId())
                        .profileImage(memberB.getMemberImage().getFilePath())
                        .name(memberB.getName())
                        .build()
        );
        this.chatResponses = chatResponses;
    }
}
