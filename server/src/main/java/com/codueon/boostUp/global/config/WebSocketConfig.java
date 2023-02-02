package com.codueon.boostUp.global.config;

import com.codueon.boostUp.domain.chat.controller.ChatRegisterController;
import com.codueon.boostUp.global.handler.StompHandler;
import com.codueon.boostUp.global.security.utils.JwtTokenUtils;
import com.codueon.boostUp.global.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final ChatRegisterController chatRegisterController;

    /**
     * 엔드포인트 및 오리진 패턴 설정 메서드
     *
     * @param registry StompEndpointRegistry
     * @author mozzi327
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // [/ws/chat] 엔드포인트로 들어온 http를 웹소켓 통신으로 전환해주는 역할을 함.
        registry.addEndpoint("/ws/chat")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    /**
     * 메시지 라우팅에 사용하는 브로커를 구성하는 메서드
     *
     * @param config MessageBrokerRegistry
     * @author mozzi327
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 발행 시 사용할 시작 포인트
        // destination header가 해당 포인트로 시작될 경우 발행 상태가 됨(StompHandler 참조)
        config.setApplicationDestinationPrefixes("/app");
        // destination header가 해당 포인트로 시작될 경우 구독 상태가 됨
        config.enableSimpleBroker("/topic", "/info");
    }

    /**
     * Security를 위한 StompIntercepter 메서드
     * @param registration
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new StompHandler(chatRegisterController));
    }


}
