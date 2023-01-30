package com.codueon.boostUp.domain.chat.service;

import com.codueon.boostUp.domain.chat.entity.MemberInChatRoom;
import com.codueon.boostUp.domain.chat.entity.MemberInfoInChatRoom;
import com.codueon.boostUp.domain.chat.repository.redis.ChatRoomRepository;
import com.codueon.boostUp.domain.chat.repository.redis.MemberInChatRoomRepository;
import com.codueon.boostUp.domain.chat.utils.ChatRoomUtils;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.global.exception.BusinessLogicException;
import com.codueon.boostUp.global.exception.ExceptionCode;
import com.codueon.boostUp.global.security.token.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomUtils chatRoomUtils;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberInChatRoomRepository memberInChatRoomRepository;

    /**
     * 사용자 채팅방 정보 저장 메서드
     * @param chatRoomId 채팅방 식별자
     * @param sessionId 세션 식별자
     * @param member 사용자 정보
     * @author mozzi327
     */
    public void saveMemberInChatRoom(Long chatRoomId, String sessionId, Member member) {
        boolean isValid = chatRoomRepository.isExistChatRoom(chatRoomId); // 채팅방 존재 유무 확인
        if (!isValid) makeChatRoom(chatRoomId);
        MemberInChatRoom memberInChatRoom = MemberInChatRoom.builder()
                .displayName(member.getName())
                .email(member.getEmail())
                .build();
        memberInChatRoomRepository.addMember(chatRoomId, sessionId, memberInChatRoom);
    }

    /**
     * 채팅방 중복 여부 확인한 후 채팅방을 생성해주는 메서드
     * @param chatRoomId 채팅방 식별자
     * @author mozzi327
     */
    private void makeChatRoom(Long chatRoomId) {
        Set<String> chatRooms = chatRoomRepository.findAllChatRoomKey();
        int numberOfChatRooms = chatRooms.size();
        if (numberOfChatRooms + 1 != chatRoomId)
            throw new BusinessLogicException(ExceptionCode.INVALID_CHAT_ROOM_ID);
        chatRoomRepository.createChatRoom(chatRoomId);
    }

    /**
     * 채팅방에 해당 유저가 존재하는지 확인하는 메서드
     * @param chatRoomId 채팅방 식별자
     * @param email 이메일 정보
     * @return boolean
     * @author mozzi327
     */
    public boolean isMemberInChatRoom(Long chatRoomId, String email) {
        List<MemberInChatRoom> members = memberInChatRoomRepository.findAllByChatRoomId(chatRoomId);
        for (MemberInChatRoom member : members) {
            if (member.getEmail().equals(email)) return true;
        }
        return false;
    }

    public MemberInfoInChatRoom deleteMemberFromChatRoom(String sessionId) {
        Set<String> chatRoomKeys = chatRoomRepository.findAllChatRoomKey();
        MemberInfoInChatRoom memberInfo = new MemberInfoInChatRoom();
        chatRoomKeys.stream().forEach(chatRoomKey -> {
            boolean isContained = memberInChatRoomRepository.contain(chatRoomKey, sessionId);
            if (isContained) {
                MemberInChatRoom member = memberInChatRoomRepository.findBySessionId(chatRoomKey, sessionId);
                memberInfo.setChatRoomId(chatRoomUtils.parseChatRoomId(chatRoomKey));
                memberInfo.setMember(member);
            }
        });
        return memberInfo;
    }
}
