package com.bikrantj.shared.utils;

import java.security.SecureRandom;

public final class IdGenerator {

    private static final String CHARSET =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                    
                    "0123456789";


    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private IdGenerator() {
        // Utility class
    }


    public static String generateWorkspaceId(int length) {
        return internal_generateWorkspaceId(length);
    }

    public static String generateWorkspaceId() {
        return internal_generateWorkspaceId(Constants.ID_LENGTH);
    }

    private static String internal_generateWorkspaceId(int length) {
        StringBuilder id = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = SECURE_RANDOM.nextInt(CHARSET.length());
            id.append(CHARSET.charAt(index));
        }

        return id.toString();
    }
}