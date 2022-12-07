package com.yeoboya.lunch.api.testContainer;

import org.junit.ClassRule;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class ContainerBaseTest extends IntegrationTest {

	// redis docker image
    private static final String DOCKER_REDIS_IMAGE = "redis:7-alpine";

	// dynamodb docker image
    private static final String DOCKER_MARIADB_IMAGE = "mariadb:10.6.10";

    @ClassRule
    static final GenericContainer REDIS_CONTAINER;

    @ClassRule
    public static GenericContainer MARIA_DB_CONTAINER;

    static {

        REDIS_CONTAINER = new GenericContainer<>(DOCKER_REDIS_IMAGE)
                .withExposedPorts(6379)
                .withReuse(true);

        MARIA_DB_CONTAINER = new GenericContainer<>(DOCKER_MARIADB_IMAGE)
        		.withExposedPorts(8000)
                .withReuse(true);

        REDIS_CONTAINER.start();
        MARIA_DB_CONTAINER.start();
    }


	// 동적 설정값 매핑
    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry){

        // redis
        registry.add("spring.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.redis.port", () -> "" + REDIS_CONTAINER.getMappedPort(6379));

    }
}