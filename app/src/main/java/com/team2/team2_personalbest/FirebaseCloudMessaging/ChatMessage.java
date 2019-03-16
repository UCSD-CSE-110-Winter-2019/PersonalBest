package com.team2.team2_personalbest.FirebaseCloudMessaging;

/*
 * function: creating chat message
 */
public class ChatMessage {
    private String from;
    private String text;

    /*
     * constructor
     */
    public ChatMessage(String from, String text) {
        this.from = from;
        this.text = text;
    }

    /*
     * function: get user name
     */
    public String getFrom() {
        return from;
    }

    /*
     * function: get text
     */
    public String getText() {
        return text;
    }

    /*
     * fucntion: concatanate the strings
     */
    public String toString() {
        return from +
                ":\n" +
                text +
                "\n" +
                "---\n";
    }
}
