package com.company;

public class ChatManager implements ClientThread.ChatListener {

    @Override
    public void onMessageReceived(String message) {
        System.out.println(message);
    }
}
