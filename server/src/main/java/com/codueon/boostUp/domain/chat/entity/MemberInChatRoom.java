package com.codueon.boostUp.domain.chat.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class MemberInChatRoom implements Serializable {
    private String displayName;
    private String email;

    @Builder
    public MemberInChatRoom(String displayName, String email) {
        this.displayName = displayName;
        this.email = email;
    }
}
