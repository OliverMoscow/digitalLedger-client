package com.CC.digitalLedger;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.io.*;

public class Window {
    public Window() throws UnsupportedOperationException {

        //FRAME
        JFrame newFrame = new JFrame("Welcome to the Gold Card Money Transferring System");
        newFrame.setSize(800, 600);
        newFrame.setVisible(true);
        newFrame.setDefaultCloseOperation(3);

        //BUTTON
        JButton userButton = new JButton("Find User");
        newFrame.add(userButton);
        userButton.setBounds(520, 180, 200, 50);

        JButton sendButton = new JButton("Transfer Funds");
        sendButton.setBounds(375, 300, 175, 50);;

        //TEXT FIELD
        JTextField publicKeyInsert = new JTextField();
        newFrame.add(publicKeyInsert);
        publicKeyInsert.setBounds(100, 180, 400, 50);

        JTextField displayTransactions = new JTextField();
        newFrame.add(displayTransactions);
        displayTransactions.setBounds(100, 275, 400, 150);

        JTextField displayPublicKey = new JTextField();
        newFrame.add(displayPublicKey);
        displayPublicKey.setBounds(100, 100, 400, 50);

        JTextField displayFunds = new JTextField();
        displayFunds.setBounds(200, 300, 400, 150);

        //JLABEL
        JLabel currentBalance = new JLabel("Current Balance:");
        newFrame.add(currentBalance);
        currentBalance.setBounds(100, 50, 700, 75);

        JLabel enterPublicKey = new JLabel("Enter the user account number: "); //finds user based on public key
        newFrame.add(enterPublicKey);
        enterPublicKey.setBounds(100, 130, 700, 75);

        JLabel transactionHistory = new JLabel("Transaction History:");
        newFrame.add(transactionHistory);
        transactionHistory.setBounds(100, 225, 700, 75);

        String public_key = System.getenv("PUBLIC_KEY");
        if (public_key == null) {
            System.err.println("PUBLIC_KEY Environment variable is not defined!");
        } else {
            System.out.println(public_key);
        }
        JLabel publicKey = new JLabel("Public Key: " + public_key);
        newFrame.add(publicKey);
        publicKey.setBounds(100, 500, 700, 75);


        String private_key = System.getenv("PRIVATE_KEY");
        if (private_key == null) {
            System.err.println("PRIVATE_KEY Environment variable is not defined!");
        } else {
            System.out.println(private_key);
        }
        JLabel privateKey = new JLabel("Private Key: " + private_key);
        newFrame.add(privateKey);
        privateKey.setBounds(400, 500, 700, 75);



        JLabel newWindowLabel = new JLabel("              Enter amount to transfer to " + "USER");
        newWindowLabel.setBounds(200, 100, 400, 50);
        class userButtonListener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                //FIND USER PUBLIC KEY CODE HERE

                //OPEN NEW WINDOW
                JFrame amountFrame = new JFrame("User: " + "INSERT PUBLIC KEY");
                amountFrame.setVisible(true);
                amountFrame.setSize(400, 200);

                JPanel amountPanel = new JPanel();
                amountFrame.add(amountPanel);

                GridLayout grid = new GridLayout(3, 1);
                amountPanel.setLayout(grid);
                amountPanel.add(newWindowLabel);
                amountPanel.add(displayFunds);
                amountPanel.add(sendButton);

            }
        }
        userButton.addActionListener(new userButtonListener());
    }
}
//    JTextField textField;

//        final JPanel panel = new JPanel();
//        this.thisFrame.add(panel);
//        JLabel label = new JLabel();
//        panel.add(label);
//        Dimension size = label.getPreferredSize();
//        label.setBounds(150, 100, size.width, size.height);
//        JButton enterButton = new JButton("Send");
//        panel.add(enterButton);
//        this.textField = new JTextField();
//        panel.add(this.textField);
//        this.textField.setColumns(20);
////
////        class enterText implements ActionListener {
////            enterText() {
////            }
////
////            public void actionPerformed(ActionEvent e) {
////                Generate tryGenerating = new Generate();
////                String[] phrasesToList = Generate.generateText(Window.this.textField.getText(), false);
////
////                for(int i = 0; i < phrasesToList.length; ++i) {
////                    JLabel thisLabel = new JLabel(phrasesToList[i] + ",");
////                    panel.add(thisLabel);
////                }
////
////            }
////        }
////
////        enterButton.addActionListener(new enterText());
////        label.setText("Enter at least one word (in English or Korean): " + this.textField.getText());
//    }

