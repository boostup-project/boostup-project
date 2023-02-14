package com.codueon.boostUp.domain.chat.repository.querydsl;

import com.codueon.boostUp.domain.chat.dto.RedisChat;

import java.util.List;

public interface CustomChatRepository {
    List<RedisChat> findTop30ChatByChatRoomId(Long chatRoomId);
}
