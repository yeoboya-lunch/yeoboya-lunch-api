package com.yeoboya.lunch.config.datasources;

import com.zaxxer.hikari.HikariDataSource;
import org.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

import static com.yeoboya.lunch.config.aws.AwsSecretsManagerClient.getSecret;

@Profile("prod")
@Configuration
public class DatabaseConfig {

    @Bean
    public DataSource dataSource() {
        JSONObject secretString = getSecret("rds!db-23f8738b-047f-42ae-9d1e-6d2199409c2c");

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mariadb://yeoboya-lunch-db.cc1j8x3t3gto.ap-northeast-2.rds.amazonaws.com:3306/lunch?characterEncoding=UTF-8");
        dataSource.setUsername(secretString.getString("username"));
        dataSource.setPassword(secretString.getString("password"));

        return dataSource;
    }

}
