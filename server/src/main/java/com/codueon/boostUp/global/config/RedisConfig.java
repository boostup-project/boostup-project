package com.codueon.boostUp.global.config;

import com.codueon.boostUp.domain.chat.utils.RedisSubscriber;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    @Value("${redis.host}")
    private String redisHost;

    @Value("${redis.port}")
    private int redisPort;

    private final ObjectMapper objectMapper;

    @Bean("messageListenerAdapter")
    public MessageListenerAdapter messageListenerAdapter(RedisSubscriber redisSubscriber) {
        return new MessageListenerAdapter(redisSubscriber, "sendMessage");
    }

    @Bean("alarmListenerAdapter")
    public MessageListenerAdapter alarmListenerAdapter(RedisSubscriber redisSubscriber) {
        return new MessageListenerAdapter(redisSubscriber, "sendAlarm");
    }

    @Bean("firstAlarmListenerAdapter")
    public MessageListenerAdapter firstAlarmListenerAdapter(RedisSubscriber redisSubscriber) {
        return new MessageListenerAdapter(redisSubscriber, "sendEnterAlarm");
    }

    /**
     * Redis Lettuce 설정 빈 메서드(비동기)
     * @return RedisConnectionFactory
     * @author mozzi327
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // [AWS 서버용]
//        RedisClusterConfiguration clusterConfiguration = new RedisClusterConfiguration();
//        clusterConfiguration.clusterNode(redisHost, redisPort);
//        return new LettuceConnectionFactory(clusterConfiguration);

        // [Local 서버용]
        return new LettuceConnectionFactory(redisHost, redisPort);
    }

    /**
     * Redis 메시지 컨테이너 빈 메서드
     * @param factory RedisConnectionFactory
     * @return RedisMessageListenerContainer
     * @author mozzi327
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory factory,
                                                                       @Qualifier("messageListenerAdapter") MessageListenerAdapter sendMessageAdapter,
                                                                       @Qualifier("firstAlarmListenerAdapter") MessageListenerAdapter enterAlarmMessageAdapter,
                                                                       @Qualifier("alarmListenerAdapter") MessageListenerAdapter alarmMessageAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        container.addMessageListener(sendMessageAdapter, new PatternTopic("chat"));
        container.addMessageListener(alarmMessageAdapter, new PatternTopic("alarm"));
        container.addMessageListener(enterAlarmMessageAdapter, new PatternTopic("firstAlarm"));
        return container;
    }

    /**
     * RedisTemplete 등록 빈 메서드
     * @return RedisTemplete(Object, Object)
     * @author mozzi327
     */
    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        return redisTemplate;
    }
}
