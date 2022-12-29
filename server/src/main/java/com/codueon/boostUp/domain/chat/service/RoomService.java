package com.codueon.boostUp.domain.chat.service;

import com.codueon.boostUp.domain.chat.redis.RedisSubscriber;
import com.codueon.boostUp.domain.chat.repository.ChatRepository;
import com.codueon.boostUp.domain.chat.repository.RoomRepository;
import com.codueon.boostUp.domain.member.service.MemberDbService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {
    private final ChatRepository chatRepository;
    private final RoomRepository roomRepository;
    private final MemberDbService memberDbService;
    private final Map<String, ChannelTopic> topics;
    private final RedisMessageListenerContainer redisMessageListener;
    private final RedisSubscriber redisSubscriber;

}
