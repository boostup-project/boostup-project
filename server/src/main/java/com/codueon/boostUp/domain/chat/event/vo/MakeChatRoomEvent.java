package com.codueon.boostUp.domain.chat.event.vo;

import com.codueon.boostUp.domain.vo.AuthInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MakeChatRoomEvent {
    /* 과외 신청 시 채팅방을 생성하기 위한 Event */
    /* 학생-선생님 채팅방 생성과 선생님 채팅방 리스트 최신화, 채팅방 입장 메시지 저장 및 전송을 수행한다. */
    private AuthInfo authInfo;
    private Long lessonId;

    @Builder
    public MakeChatRoomEvent(AuthInfo authInfo, Long lessonId) {
        this.authInfo = authInfo;
        this.lessonId = lessonId;
    }
}
