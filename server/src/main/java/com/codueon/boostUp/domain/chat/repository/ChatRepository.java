package com.codueon.boostUp.domain.chat.repository;

import com.codueon.boostUp.domain.chat.entity.ChatMessage;
import com.codueon.boostUp.domain.chat.entity.ChatRoom;
import com.codueon.boostUp.domain.chat.redis.RedisSubscriber;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.codueon.boostUp.domain.chat.entity.QChatMessage.chatMessage;
import static com.codueon.boostUp.domain.chat.entity.QChatRoom.chatRoom;

@Repository
public class ChatRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager em;

    public ChatRepository(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
        this.em = em;
    }

    public List<ChatMessage> findAllLatestChats(List<ChatRoom> rooms) {
        return jpaQueryFactory
                .selectFrom(chatMessage)
                .where(Expressions.list(chatMessage.room, chatMessage.createdAt)
                        .in((JPAExpressions
                                .select(chatMessage.room, chatMessage.createdAt.max())
                                .from(chatMessage)
                                .where(chatMessage.room.in(rooms))
                                .groupBy(chatMessage.room)
                        )))
                .orderBy(chatMessage.createdAt.desc())
                .fetch();
    }

    public void save(ChatMessage message) {
        em.persist(message);
    }

    public List<ChatMessage> findAllChatsInRoom(Long roomId) {
        return jpaQueryFactory.select(chatMessage)
                .from(chatMessage)
                .where(chatMessage.room.id.eq(roomId))
                .fetch();
    }

    public Optional<ChatRoom> getOrCreate(Long merChantId, Long studentId) {
        return Optional.ofNullable(jpaQueryFactory
                        .selectFrom(chatRoom)
                        .where(chatRoom.student.id.eq(studentId).and(chatRoom.merchant.id.eq(merChantId)))
                .fetchFirst());
    }
}
