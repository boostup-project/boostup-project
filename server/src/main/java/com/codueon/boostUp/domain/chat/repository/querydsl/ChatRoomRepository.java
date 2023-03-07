package com.codueon.boostUp.domain.chat.repository.querydsl;

import com.codueon.boostUp.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Boolean existsBySenderIdAndReceiverId(Long firstId, Long secondId);
    Optional<ChatRoom> findBySenderIdAndReceiverId(Long senderId, Long receiverId);
    List<ChatRoom> findChatRoomsBySenderIdOrReceiverId(Long senderId, Long receiverId);
    void deleteAllBySenderIdOrReceiverId(Long senderId, Long memberId);
}
