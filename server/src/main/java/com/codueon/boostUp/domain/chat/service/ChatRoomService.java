package com.codueon.boostUp.domain.chat.service;

import com.codueon.boostUp.domain.chat.repository.redis.ChatRoomRepository;
import com.codueon.boostUp.domain.chat.utils.ChatRoomUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUtils chatRoomUtils;
}
