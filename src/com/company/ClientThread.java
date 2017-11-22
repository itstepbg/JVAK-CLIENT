package com.company;

import com.company.model.API;
import com.company.model.User;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientThread extends Thread {
    //Producer-Consumer
    private LinkedBlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
    String lastMessage = "";

    private DatagramSocket clientSocket;
    private ChatListener chatListener;

    byte[] buffer = new byte[256];
    InetAddress serverAddress;
    int serverPort = 3000;
    int port = 4000;

    public ClientThread(String name) {
        super(name);
        try {
            clientSocket = new DatagramSocket(port);
            clientSocket.setSoTimeout(1000);
            serverAddress = InetAddress.getByName("127.0.0.1");
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();

        try {
            lastMessage = messageQueue.take();
            sendMessage(lastMessage);

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, serverPort);

            clientSocket.receive(packet);
            String request = new String(packet.getData());
            handleResponse(request);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message) {
        byte[] buffer;
        buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, 3000);

        try {
            clientSocket.send(packet);
        } catch (IOException e) {
            //TODO
        }
    }

    private void handleResponse(String response) {
        //action (prefix)
        String prefix = response.split(":")[0];
        //value (payload)
        String payload = response.split(":")[1];

        chatListener.onMessageReceived(response);

        switch (prefix) {
            case API.PREFIX_CHAT_MESSAGE:

                break;
            case API.PREFIX_STATUS:
                String lastMessagePrefix = lastMessage.split(":")[0];
                switch (lastMessagePrefix) {
                    case API.PREFIX_LOGIN:
                        if (payload.equals(API.STATUS_OK)) {
                            User me = new User();
                            UserManager.getInstance().setUser(me);
                        } else {
                            UserManager.getInstance().setUser(null);
                        }
                        break;
                }
                lastMessage = "";
                break;
        }
    }

    public LinkedBlockingQueue<String> getMessageQueue() {
        return messageQueue;
    }

    public boolean isLoginInProgress() {
        if (lastMessage.startsWith(API.PREFIX_LOGIN)) {
            return true;
        } else {
            return false;
        }
    }

    public void setChatListener(ChatListener chatListener) {
        this.chatListener = chatListener;
    }

    public interface ChatListener {
        //Call-back
        void onMessageReceived(String message);
    }
}