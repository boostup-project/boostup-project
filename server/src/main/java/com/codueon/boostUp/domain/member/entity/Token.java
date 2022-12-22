package com.codueon.boostUp.domain.member.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Token {
    @Id
    @Column(name = "TOKEN_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tokenValue;
    private Long memberId;

    @Builder
    public Token(Long id,
                 String tokenValue,
                 Long memberId) {
        this.id = id;
        this.tokenValue = tokenValue;
        this.memberId = memberId;
    }
}
