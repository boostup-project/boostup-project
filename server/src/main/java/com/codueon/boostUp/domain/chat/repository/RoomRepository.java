package com.codueon.boostUp.domain.chat.repository;

import com.codueon.boostUp.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface RoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findByMerchantIdAndStudentId(Long merchantId, Long studentId);
}
