package com.yeoboya.lunch.config;

import com.yeoboya.lunch.config.aws.AwsSecretsManagerClient;
import org.json.JSONObject;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

public class SecretsManagerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        try {
            JSONObject secrets = AwsSecretsManagerClient.getSecret("prod/lunch");
            String password = secrets.getString("jasypt.encryptor.password");

            TestPropertyValues
                .of("jasypt.encryptor.password=" + password)
                .applyTo(environment);

        } catch (Exception ignored) { }
    }
}
