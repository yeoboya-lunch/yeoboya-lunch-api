package com.yeoboya.lunch.api.container;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Testcontainers
@Slf4j
public class ContainerDI extends IntegrationDI {

    @Container
    private static final MariaDBContainer<?> mariaDBContainer = new MariaDBContainer<>("mariadb:10.6.10").withReuse(true);


    static {
        GenericContainer<?> REDIS_CONTAINER = new GenericContainer<>(DockerImageName.parse("redis:7-alpine"))
                .withExposedPorts(6379)
                .withReuse(true);

        REDIS_CONTAINER.start();

        System.setProperty("spring.redis.master.host", REDIS_CONTAINER.getHost());
        System.setProperty("spring.redis.master.port", REDIS_CONTAINER.getMappedPort(6379).toString());
    }


    @Test
    @Disabled
    void connect() {
        log.error("host: {}", mariaDBContainer.getHost());
        log.error("port: {}", mariaDBContainer.getMappedPort(3306));
        log.error("username: {}", mariaDBContainer.getUsername());
        log.error("password: {}", mariaDBContainer.getPassword());
        log.error("jdbc url: {}", mariaDBContainer.getJdbcUrl());
        try (Connection conn = DriverManager.getConnection(mariaDBContainer.getJdbcUrl(), mariaDBContainer.getUsername(), mariaDBContainer.getPassword())
        ) {
            log.error("got connection");
            // 코드
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
