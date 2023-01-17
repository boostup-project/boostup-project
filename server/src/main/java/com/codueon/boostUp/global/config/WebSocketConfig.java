package com.codueon.boostUp.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 엔드포인트 및 오리진 패턴 설정 메서드
     *
     * @param registry StompEndpointRegistry
     * @author mozzi327
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // [/ws-connect] 엔드포인트로 들어온 http를 웹소켓 통신으로 전환해주는 역할을 함.
        registry.addEndpoint("/ws-connect")
                .setAllowedOriginPatterns("*")
                .setAllowedOrigins("*")
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
        config.setApplicationDestinationPrefixes("/pub");
        // destination header가 해당 포인트로 시작될 경우 구독 상태가 됨
        config.enableSimpleBroker("/queue", "/topic");
    }
}
