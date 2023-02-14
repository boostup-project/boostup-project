package com.codueon.boostUp.domain.chat.repository.querydsl;

import com.codueon.boostUp.domain.chat.dto.QRedisChat;
import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.codueon.boostUp.domain.chat.entity.QChatMessage.chatMessage;

@Repository
public class ChatRepositoryImpl implements CustomChatRepository {
    private final JPAQueryFactory queryFactory;

    public ChatRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 레디스 챗 메시지 유실 시 30개 조회 및 저장 메서드
     *
     * @param chatRoomId 채팅방 식별자
     * @return List(RedisChat)
     * @author mozzi327
     */
    @Override
    public List<RedisChat> findTop30ChatByChatRoomId(Long chatRoomId) {
        return queryFactory.select(new QRedisChat(
                        chatMessage.chatRoomId,
                        chatMessage.senderId,
                        chatMessage.message,
                        chatMessage.messageType,
                        chatMessage.displayName,
                        chatMessage.createdAt
                )).from(chatMessage)
                .where(chatMessage.chatRoomId.eq(chatRoomId))
                .limit(30)
                .orderBy(chatMessage.createdAt.asc())
                .fetch();
    }
}
