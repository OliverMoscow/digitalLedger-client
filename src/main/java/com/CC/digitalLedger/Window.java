package com.CC.digitalLedger;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;

public class Window {
    JTextField inputUser = new JTextField();
    Backup backup;
    ServerRequest server;

    public Window() throws Exception {
        backup = new Backup();
        //GENERATION PAGE
        if (backup.isInitialized == false) {
            File f = new File("secret.txt");

            //GENERATE USER PAGE
            JFrame createFrame = new JFrame("Generation Page");
            createFrame.setSize(600, 400);
            createFrame.setVisible(true);
            createFrame.setDefaultCloseOperation(3);
            JButton createButton = new JButton("Create Account");
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
                    try {
                        backup.save(inputUser.getText());
                        ServerRequest server = new ServerRequest("https://8ce2-192-70-253-79.ngrok.io", backup);
                        System.out.println(server.instantiateUser());
                    } catch (Exception e2) {
                        System.out.println("Failed to retrieve user info");
                    }
                    try {
                        createMainGUI();
                    }catch (Exception e4){
                        System.out.println("Gui didn't work");
                    }
                    createFrame.setVisible(false);
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
        if(server == null) {
            server = new ServerRequest("https://8ce2-192-70-253-79.ngrok.io", backup);
        }
        JFrame newFrame = new JFrame("Ledger System");
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

        JButton refreshBalance = new JButton("Refresh");

        JLabel currentBalance = new JLabel("Current Balance:");
        JLabel enterPublicKey = new JLabel("Search user by name: "); //finds user based on public key or name
        JLabel transactionHistory = new JLabel("Transaction History:");
        JLabel publicKey = new JLabel();
        publicKey.setText("Public Key: " + server.secret.publicKeyAsString());
        JLabel privateKey = new JLabel("Private Key: ");
        privateKey.setText("Private Key: " + server.secret.privateKeyAsString());
        JLabel welcomeUser = new JLabel();
        welcomeUser.setText("Welcome to the Ledger System, " + backup.name);
        welcomeUser.setVisible(true);

        JLabel serverPort = new JLabel("Server port is https://8ce2-192-70-253-79.ngrok.io");

        newFrame.setSize(900, 600);
        newFrame.setVisible(true);

        newFrame.add(newPanel);
        GridLayout newGrid = new GridLayout(12, 1);
        newPanel.setLayout(newGrid);
        newPanel.add(serverPort);
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
        newPanel.add(refreshBalance);
        newPanel.revalidate();

        JFrame amountFrame = new JFrame("Transfer Funds");
        JButton sendButton = new JButton("Transfer Funds");

        JTextField displayFunds = new JTextField(); //for the second window

        class userButtonListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    server.getUserFromName(getUsers.getText());
                } catch (Exception e3) {
                    System.out.println("Failed to retrieve user. Account probably doesn't exist");
                }
                amountFrame.setVisible(true);
                amountFrame.setSize(400, 200);
                JPanel amountPanel = new JPanel();
                amountFrame.add(amountPanel);
                JLabel newWindowLabel = new JLabel("Enter amount: ");
                JLabel otherUser = new JLabel();
                try {
                otherUser.setText("User: " + server.getUserFromName(getUsers.getText()));
                } catch (Exception e3) {
                    System.out.println("server.getUserFromName(getUsers.getText()) failed");
                }
                GridLayout grid = new GridLayout(4, 1);
                amountPanel.setLayout(grid);
                amountPanel.add(otherUser);
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
                    System.out.println(server.send(Double.valueOf(displayFunds.getText()), getUsers.getText()));
                } catch (Exception e1) {
                    System.out.println("Transaction did not complete");
                }
                amountFrame.dispose();
            }
        }
        sendButton.addActionListener(new sendButtonListener());

        class refreshButtonListener implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e) {
                newFrame.dispose();
                try {
                    createMainGUI();
                }catch(Exception e7){
                        System.out.println("You can't do this");
                    }
            }
        }
        refreshBalance.addActionListener(new refreshButtonListener());
    }
}


