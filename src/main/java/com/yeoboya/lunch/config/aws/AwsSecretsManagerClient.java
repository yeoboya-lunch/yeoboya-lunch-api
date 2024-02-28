package com.yeoboya.lunch.config.aws;

import org.json.JSONObject;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

public class AwsSecretsManagerClient {

    public static JSONObject getSecret(String secretName) {

        SecretsManagerClient secretsManagerClient = SecretsManagerClient.builder()
                .region(software.amazon.awssdk.regions.Region.AP_NORTHEAST_2)
                .build();

        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();

        GetSecretValueResponse getSecretValueResponse = secretsManagerClient.getSecretValue(getSecretValueRequest);
        String secret = getSecretValueResponse.secretString();
        return new JSONObject(secret);
    }
}
