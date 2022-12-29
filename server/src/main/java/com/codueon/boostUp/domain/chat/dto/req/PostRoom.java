package com.codueon.boostUp.domain.chat.dto.req;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class PostRoom {
    @NotNull
    private Long merchantId;
    @NotNull
    private Long studentId;

    @Builder
    public PostRoom(Long merchantId, Long studentId) {
        this.merchantId = merchantId;
        this.studentId = studentId;
    }
}
