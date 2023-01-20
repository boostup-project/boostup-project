package com.codueon.boostUp.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class PostRoom {
    @NotNull
    private Long tutorId;
    @NotNull
    private Long studentId;

    @Builder
    public PostRoom(Long tutorId, Long studentId) {
        this.tutorId = tutorId;
        this.studentId = studentId;
    }
}
