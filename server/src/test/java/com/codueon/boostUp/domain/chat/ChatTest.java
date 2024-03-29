package com.codueon.boostUp.domain.chat;

import com.codueon.boostUp.domain.IntegrationTest;
import com.codueon.boostUp.domain.chat.service.ChatAlarmService;
import com.codueon.boostUp.domain.chat.service.ChatRoomService;
import com.codueon.boostUp.domain.chat.service.ChatService;
import com.codueon.boostUp.domain.lesson.repository.LessonRepository;
import com.codueon.boostUp.domain.member.repository.MemberRepository;
import com.codueon.boostUp.domain.suggest.repository.SuggestRepository;
import com.codueon.boostUp.global.security.utils.JwtTokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
public class ChatTest extends IntegrationTest {
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected JwtTokenUtils jwtTokenUtils;
    @Autowired
    protected ChatService chatService;
    @Autowired
    protected ChatRoomService chatRoomService;
    @Autowired
    protected ChatAlarmService chatAlarmService;
    @Autowired
    protected MemberRepository memberRepository;
    @Autowired
    protected LessonRepository lessonRepository;
    @Autowired
    protected SuggestRepository suggestRepository;
    protected String tutorToken;
    protected String studentToken;
}
