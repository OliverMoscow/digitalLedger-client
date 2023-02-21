package com.CC.digitalLedger;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;

public class Window {
    JTextField inputUser = new JTextField();

    public Window() throws Exception {

        //GENERATION PAGE
        if (Backup.isInitialized() == false) {
            Secret newSecret = new Secret(); //runs constructor for secret
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
                    //open new window
                    inputUser.getText(); //Name is already on server
                    try {
                        ServerRequest server = new ServerRequest("https://8ce2-192-70-253-79.ngrok.io");
                        server.instantiateUser(inputUser.getText());
                        Backup.save(newSecret, f,inputUser.getText());
                        Backup.load();
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
        ServerRequest server = new ServerRequest("https://8ce2-192-70-253-79.ngrok.io");
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
        JLabel transactionHistory = new JLabel("Transaction History:");
        JLabel publicKey = new JLabel();
        publicKey.setText("Public Key: " + server.secret.publicKeyAsString());
        JLabel privateKey = new JLabel("Private Key: ");
        privateKey.setText("Private Key: " + server.secret.privateKeyAsString());
        JLabel welcomeUser = new JLabel();
        welcomeUser.setText("Welcome to the Gold Card Money Transferring System, " + inputUser.getText());
        welcomeUser.setVisible(true);

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
                    //probably need to do a boolean for this like boolean getUser == true, then open window, else Jframe says not true
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
                    server.send(displayFunds.getText(), getUsers.getText());
                } catch (Exception e1) {
                    System.out.println("Send() doesn't work");
                }
                amountFrame.dispose();
            }
        }
        sendButton.addActionListener(new sendButtonListener());
    }
}


