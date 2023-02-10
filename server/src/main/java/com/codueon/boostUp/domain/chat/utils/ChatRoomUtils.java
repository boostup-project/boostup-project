package com.codueon.boostUp.domain.chat.utils;

import com.codueon.boostUp.domain.chat.vo.ParseMemberVO;
import org.springframework.stereotype.Component;

@Component
public class ChatRoomUtils {
    private final String PREFIX_OF_KEY = "ChatRoom";
    private final String SUFFIX_OF_KEY = "Message";
    private final String PREFIX_OF_MEMBER = "Member";

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
     * member key 형변환 메서드(저장용)
     * @param memberId 사용자 식별자
     * @return String
     * @author mozzi327
     */
    public String makeMemberKey(Long memberId) {
        return PREFIX_OF_MEMBER + memberId + SUFFIX_OF_KEY;
    }
}
