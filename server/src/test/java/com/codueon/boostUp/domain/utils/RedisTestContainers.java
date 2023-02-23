package com.codueon.boostUp.domain.utils;

import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class RedisTestContainers {
    private static final String REDIS_DOCKER_NAME = "redis:6-alpine";
    static final GenericContainer<?> REDIS_CONTAINER;

    static {
        REDIS_CONTAINER =
                new GenericContainer<>(DockerImageName.parse(REDIS_DOCKER_NAME))
                        .withExposedPorts(6379)
                        .withReuse(true);
        REDIS_CONTAINER.start();
    }

    @DynamicPropertySource
    public static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("redis.host", REDIS_CONTAINER::getHost);
        registry.add("redis.port", () -> "" + REDIS_CONTAINER.getMappedPort(6379));
    }
}
