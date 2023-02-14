package com.codueon.boostUp.domain.chat.repository.querydsl;

import com.codueon.boostUp.domain.chat.dto.RedisChat;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatJdbcRepository {
    private final JdbcTemplate jdbcTemplate;

    public void batchInsertChatMessages(List<RedisChat> redisChats) {
        String sql = "INSERT INTO CHAT_MESSAGE"
                + "(chat_room_id, sender_id, message, display_name, message_type, created_at) " +
                "VALUE(?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                RedisChat redisChat = redisChats.get(i);
                ps.setLong(1, redisChat.getChatRoomId());
                ps.setLong(2, redisChat.getSenderId());
                ps.setString(3, redisChat.getMessage());
                ps.setString(4, redisChat.getDisplayName());
                ps.setString(5, redisChat.getMessageType().toString());
                ps.setObject(6, redisChat.getCreatedAt());
            }

            @Override
            public int getBatchSize() {
                return redisChats.size();
            }
        });
    }
}
