package com.CC.digitalLedger;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;


public class Window {
    public Window() throws Exception {
        //GENERATION PAGE
        if (Backup.isInitialized() == false) {
            Secret newSecret = new Secret(); //runs constructor for secret
            File f = new File("secret.txt");
            Backup.save(newSecret, f);

            //GENERATE USER PAGE
            JFrame createFrame = new JFrame("Generation Page");
            createFrame.setSize(600, 400);
            createFrame.setVisible(true);
            createFrame.setDefaultCloseOperation(3);
            JButton createButton = new JButton("Create Account");
            JTextField inputUser = new JTextField();
            JLabel information = new JLabel();
            information.setText("<html>" + "Welcome to the Gold Card money transferring website! By clicking 'Create Account', we will have generated a pair of public and private keys for you, which you will be able to see on the home page. DO NOT SHARE YOUR PRIVATE KEY INFORMATION." + "</html>");
            JLabel createUser = new JLabel("Input your name: ");

            JPanel createPanel = new JPanel();
            createFrame.add(createPanel);
            GridLayout loginGrid = new GridLayout(6, 1);
            createPanel.setLayout(loginGrid);
            createPanel.add(createUser);
            createPanel.add(inputUser);
            createPanel.add(information);
            createPanel.add(createButton);

            class createButtonListener implements ActionListener {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //open new window
                    inputUser.getText(); //Name is already on server
                    try {
                        ServerRequest server = new ServerRequest("https://c956-192-70-253-79.ngrok.io");
                        server.instantiateUser(inputUser.getText());
                        Backup.load();
                    } catch (Exception e2) {
                        System.out.println("Failed to retrieve user info");
                    }
                    try {
                        createMainGUI();
                    }catch (Exception e4){
                        System.out.println("Gui didn't work");
                    }
                }
            }
            createButton.addActionListener(new createButtonListener());
            //makes everything appear on GUI window
            createPanel.revalidate();
        }else{
            createMainGUI();
        }
    }
    void createMainGUI() throws Exception{
        ServerRequest server = new ServerRequest("https://7e65-192-70-253-78.ngrok.io");
        JFrame newFrame = new JFrame("Gold Card Money Transferring System");
        newFrame.setDefaultCloseOperation(3);
        JPanel newPanel = new JPanel();
        JTextField getUsers = new JTextField();

        JTextArea displayTransactions = new JTextArea();
        displayTransactions.setWrapStyleWord(true);
        displayTransactions.setLineWrap(true);
        displayTransactions.setText(server.getLedger());
        JTextArea displayBalance = new JTextArea();
        displayBalance.setText(server.getBalance());
        JButton userButton = new JButton("Find user");
        JLabel currentBalance = new JLabel("Current Balance:");
        JLabel enterPublicKey = new JLabel("Search user by name: "); //finds user based on public key or name
        //NEED TO STORE THE USER YOU SEARCHED


        JLabel transactionHistory = new JLabel("Transaction History:");
        JLabel publicKey = new JLabel("Public Key: ");
        publicKey.setText("<html>" + server.secret.publicKeyAsString() + "</html>");
        JLabel privateKey = new JLabel("Private Key: ");
        privateKey.setText("<html>" + server.secret.privateKeyAsString() + "</html>");
        JLabel welcomeUser = new JLabel("Welcome to the Gold Card Money Transferring System, " + server.currentUser());

        newFrame.setSize(900, 600);
        newFrame.setVisible(true);

        newFrame.add(newPanel);
        GridLayout newGrid = new GridLayout(10, 1);
        newPanel.setLayout(newGrid);
        newPanel.add(welcomeUser);
        newPanel.add(currentBalance);
        newPanel.add(displayBalance);
        newPanel.add(enterPublicKey);
        newPanel.add(getUsers);
        newPanel.add(userButton);
        newPanel.add(transactionHistory);
        newPanel.add(displayTransactions);
        newPanel.add(publicKey);
        newPanel.add(privateKey);
        newPanel.revalidate();
        //createFrame.setVisible(false);

        JFrame amountFrame = new JFrame("User: " + server.currentUser());
        JButton sendButton = new JButton("Transfer Funds");

        class userButtonListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                //OPEN NEW WINDOW
                getUsers.getText();
                try {
                    server.getUserFromName(getUsers.getText());
                } catch (Exception e3) {
                    System.out.println("Failed to retrieve user name. Please try again");
                }
                amountFrame.setVisible(true);
                amountFrame.setSize(400, 200);

                //make sure this specific window is with that specific user
                JPanel amountPanel = new JPanel();
                amountFrame.add(amountPanel);
                JTextField displayFunds = new JTextField(); //for the second window
                JLabel newWindowLabel = new JLabel("                                                  Enter amount: ");

                GridLayout grid = new GridLayout(3, 1);
                amountPanel.setLayout(grid);
                amountPanel.add(newWindowLabel);
                amountPanel.add(displayFunds);
                amountPanel.add(sendButton);
            }
        }
        userButton.addActionListener(new userButtonListener());


        class sendButtonListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    server.send();
                } catch (Exception e1) {
                    System.out.println("info didn't send. check button");
                }
                amountFrame.dispose();
                System.out.println("Money successfully transferred");
            }
        }
        sendButton.addActionListener(new sendButtonListener());
    }
}


//
//        if (Backup.isInitialized() == true){
//            createFrame.setVisible(false);
//            newFrame.setSize(900, 600);
//            newFrame.setVisible(true);
//
//            newFrame.add(newPanel);
//            GridLayout newGrid = new GridLayout(10, 1);
//            newPanel.setLayout(newGrid);
//            newPanel.add(welcomeUser);
//            newPanel.add(currentBalance);
//            newPanel.add(displayBalance);
//            newPanel.add(enterPublicKey);
//            newPanel.add(getUsers);
//            newPanel.add(userButton);
//            newPanel.add(transactionHistory);
//            newPanel.add(displayTransactions);
//            newPanel.add(publicKey);
//            newPanel.add(privateKey);
//        }
//    }
//
//}

