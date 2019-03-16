package com.team2.team2_personalbest.FirebaseCloudMessaging;

public class ChatMessage {
    private String from;
    private String text;

    public ChatMessage(String from, String text) {
        this.from = from;
        this.text = text;
    }

    public String getFrom() {
        return from;
    }

    public String getText() {
        return text;
    }

    public String toString() {
        return from +
                ":\n" +
                text +
                "\n" +
                "---\n";
    }
}
