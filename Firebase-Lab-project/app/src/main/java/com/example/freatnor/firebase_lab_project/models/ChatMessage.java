package com.example.freatnor.firebase_lab_project.models;

/**
 * Created by Jonathan Taylor on 8/23/16.
 */
public class ChatMessage {
    private String user;
    private String messageText;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
