package com.team2.team2_personalbest.FirebaseCloudMessaging;

/**
 *
 *fucntion: factory for chat message service
 */
public class ChatMessageServiceFactory extends Factory<ChatMessageService> {
    private static ChatMessageServiceFactory instance;

    /**
     *
     * @consturctor
     */
    public static ChatMessageServiceFactory getInstance() {
        if (instance == null) {
            instance = new ChatMessageServiceFactory();
        }
        return instance;
    }

}
