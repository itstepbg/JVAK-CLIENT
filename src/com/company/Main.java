package com.company;

import com.company.model.API;

import java.util.Scanner;

public class Main {

    //Init running and assign true so that we can loop with (while)
    private static boolean running = true;
    private static UserManager userManager = UserManager.getInstance();
    private static ChatManager chatManager = new ChatManager();
    private static ClientThread networkingThread;
    //
    public static void main(String[] args) {
        networkingThread = new ClientThread("networkingThread");
        networkingThread.start();

        networkingThread.setChatListener(chatManager);

        //Looping while running == true
        while(running) {
            if (userManager.isUserOnline()) {
                //Display the main menu in the console in the form of text
                showMenu();
                //Wait for the user input, and read it once the user has entered something.
                //Pass the UserManager instance to the method.
                String command = readUserInput("Select Menu Item:");
                menuSelection(command);
            } else {
                if (!networkingThread.isLoginInProgress()) {
                    attemptLogin();
                }
            }
        }
    }

    private static void showMenu() {
        //Print the menu information on the screen.
        System.out.println();
        System.out.println("1. Start Conversation");
        System.out.println("2. List Online Users");
        System.out.println("3. Logout");
    }

    private static void attemptLogin() {
        String userName = readUserInput("Please enter your User Name: ");
        String password = readUserInput("Please enter your Password: ");

        String loginMessage = API.PREFIX_LOGIN + userName + "," + password;

        networkingThread.getMessageQueue().offer(loginMessage);
    }

    private static String readUserInput(String message) {
        //Print out a line and text that get the user aware of the menu options.
        System.out.println();
        System.out.print(message);

        //Create a new instance scanner of the object Scanner that "scans" the System.in
        Scanner scanner = new Scanner(System.in);

        return scanner.next();

    }

    private static void displayError() {
        System.out.println();
        System.out.println("Invalid selection!");
    }

    private static void menuSelection(String command) {
        int commandCode = 0;
        // Using try catch to try to "convert" a string into an integer.
        // If the string is a number, commandCode will be equal to that number in integer.
        // If the string is a character, we will displayError();
        try {
            commandCode = Integer.parseInt(command);
        } catch(Exception e) {
            displayError();
        }

        //Check if the user has entered a valid option
        if (commandCode > 0) {
            //TODO Implement menu actions with switch
        } else {
            displayError();
        }
    }
}