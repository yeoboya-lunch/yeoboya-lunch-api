package com.yeoboya.lunch.config.security.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class OAuth2Attribute {
    private final Map<String, Object> attributes;
    private final String attributeKey;
    private final String email;
    private final String name;
    private final String provider;

    public static OAuth2Attribute of(String provider, String attributeKey, Map<String, Object> attributes) {
        switch (provider) {
            case "google":
                return ofGoogle(provider, attributeKey, attributes);
            case "kakao":
                return ofKakao(provider, "email", attributes);
            case "naver":
                return ofNaver(provider, "id", attributes);
            default:
                throw new IllegalArgumentException("Unsupported provider: " + provider);
        }
    }

    private static OAuth2Attribute ofGoogle(String provider, String attributeKey, Map<String, Object> attributes) {
        return OAuth2Attribute.builder()
                .email((String) attributes.get("email"))
                .name((String) attributes.get("name"))
                .provider(provider)
                .attributes(attributes)
                .attributeKey(attributeKey)
                .build();
    }

    private static OAuth2Attribute ofKakao(String provider, String attributeKey, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = getMapSafely(attributes, "kakao_account");
        Map<String, Object> kakaoProfile = getMapSafely(kakaoAccount, "profile");

        return OAuth2Attribute.builder()
                .email((String) kakaoAccount.get("email"))
                .name((String) kakaoProfile.get("nickname"))
                .provider(provider)
                .attributes(kakaoAccount)
                .attributeKey(attributeKey)
                .build();
    }

    private static OAuth2Attribute ofNaver(String provider, String attributeKey, Map<String, Object> attributes) {
        Map<String, Object> response = getMapSafely(attributes, "response");

        return OAuth2Attribute.builder()
                .email((String) response.get("email"))
                .name((String) response.get("name"))
                .provider(provider)
                .attributes(response)
                .attributeKey(attributeKey)
                .build();
    }

    // 안전한 Map 변환 (unchecked 경고 방지)
    private static Map<String, Object> getMapSafely(Map<String, Object> attributes, String key) {
        Object value = attributes.get(key);
        if (value instanceof Map<?, ?>) {
            Map<?, ?> rawMap = (Map<?, ?>) value;
            Map<String, Object> result = new HashMap<>();
            for (Map.Entry<?, ?> entry : rawMap.entrySet()) {
                if (entry.getKey() instanceof String) {
                    result.put((String) entry.getKey(), entry.getValue());
                }
            }
            return result;
        }
        return new HashMap<>();
    }

    public Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", attributeKey);
        map.put("key", attributeKey);
        map.put("email", email);
        map.put("name", name);
        map.put("provider", provider);
        return map;
    }
}