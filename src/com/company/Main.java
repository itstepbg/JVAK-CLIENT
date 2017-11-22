package com.company;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Main {

    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket();

            byte[] buffer = new byte[256];
            InetAddress address = InetAddress.getByName("192.168.100.36");
            int port = 3000;
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);

            socket.send(packet);

            packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);

            String response = new String(packet.getData(), 0, packet.getLength());
            System.out.println(response);

        } catch (SocketException e) {
            //e.printStackTrace();
        } catch (UnknownHostException e) {
            //e.printStackTrace();
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }
}