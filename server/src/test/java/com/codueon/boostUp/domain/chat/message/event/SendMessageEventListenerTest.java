package com.codueon.boostUp.domain.chat.message.event;

import com.codueon.boostUp.domain.chat.event.listener.SendMessageEventListener;
import com.codueon.boostUp.domain.chat.ChatRoomTest;
import com.codueon.boostUp.domain.chat.service.EventMessageService;
import com.codueon.boostUp.domain.lesson.repository.LessonRepository;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.utils.DataForTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SendMessageEventListenerTest extends ChatRoomTest {
    @Autowired
    protected LessonRepository lessonRepository;
    @Autowired
    protected EventMessageService eventMessageService;
    protected SendMessageEventListener sendMessageEventListener;

    @BeforeEach
    void setUp() {
        sendMessageEventListener = new SendMessageEventListener(eventMessageService);
    }

    @Test
    @DisplayName("메시지 전송 시 해당 채팅룸에 대한 메시지가 저장되어야 한다.")
    void sendMessageTest() {
        // given
        List<Member> members = List.of(DataForTest.getTutor(), DataForTest.getStudent());
        memberRepository.saveAll(members);
        lessonRepository.save(DataForTest.getSaveLesson());
    }
}
