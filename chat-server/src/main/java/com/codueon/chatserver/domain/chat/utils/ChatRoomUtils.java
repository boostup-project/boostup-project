package com.codueon.chatserver.domain.chat.utils;

import org.springframework.stereotype.Component;

@Component
public class ChatRoomUtils {
    public final String  KEY_FOR_CHAT_ROOMS = "ChatRooms";
    private final String PREFIX_OF_KEY = "ChatRoom";
    private final String SUFFIX_OF_KEY = "Session";
    private final String SUFFIX_OF_LAST_CHAT_KEY = "LastSavedKey";

    /**
     * chatRoom key 형변환 메서드(조회용)
     * @param key chatRoomId 정보
     * @return Long
     * @author mozzi327
     */
    public Long parseChatRoomId(String key) {
        String removeAddWordInKey = key
                .replace(PREFIX_OF_KEY, "")
                .replace(SUFFIX_OF_KEY, "");
        return Long.parseLong(removeAddWordInKey);
    }

    /**
     * chatRoom key 형변환 메서드(저장용)
     * @param chatRoomId 채팅방 식별자
     * @return String
     * @author mozzi327
     */
    public String makeKey(Long chatRoomId) {
        return PREFIX_OF_KEY + chatRoomId + SUFFIX_OF_KEY;
    }

    /**
     * 가장 최근 메시지 식별자 조회 메서드
     * @param chatRoomId 채팅방 식별자
     * @return String
     * @author mozzi327
     */
    public String makeLastChatMessageKey(Long chatRoomId) {
        return PREFIX_OF_KEY + chatRoomId + SUFFIX_OF_LAST_CHAT_KEY;
    }
}
