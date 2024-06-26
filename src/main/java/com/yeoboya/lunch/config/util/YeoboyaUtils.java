package com.yeoboya.lunch.config.util;

import java.util.Random;

public class YeoboyaUtils {

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateRandomString(int length) {
        Random random = new Random();
        StringBuilder builder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int characterIndex = random.nextInt(CHARACTERS.length());
            builder.append(CHARACTERS.charAt(characterIndex));
        }

        return builder.toString();
    }

}
