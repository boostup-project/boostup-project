package com.codueon.boostUp.global.security.config;

import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
    /**
     * 웹소켓 시큐리티 적용 메서드
     * @param messages MessageSecurityMetadataSourceRegistry
     * @author mozzi327
     */
    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages.simpSubscribeDestMatchers("/topic/**").hasAnyAuthority("ROLE_USER")
                .simpDestMatchers("/pub/**").hasAuthority("ROLE_USER");
    }

    /**
     * SameOrigin Disable 메서드
     * @return true;
     * @author mozzi327
     */
    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
