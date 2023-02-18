package com.CC.digitalLedger;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


public class Window {
    public Window() throws Exception {
        ServerRequest requestInfo = new ServerRequest("https://351e-192-70-253-78.ngrok.io"); //https://e2ab-192-70-253-79.ngrok.io

        //FRAME
        JFrame newFrame = new JFrame("Welcome to the Gold Card Money Transferring System");
        newFrame.setSize(900, 600);
        newFrame.setVisible(true);
        newFrame.setDefaultCloseOperation(3);


        JFrame amountFrame = new JFrame("User: " + requestInfo.getUserFromName(requestInfo.name));

         //BUTTON
        JButton sendButton = new JButton("Transfer Funds");
        JButton userButton = new JButton(requestInfo.getUserFromName(requestInfo.name));

        //TEXT AREA
        JTextArea displayTransactions = new JTextArea();
        displayTransactions.setWrapStyleWord(true);
        displayTransactions.setLineWrap(true);
        displayTransactions.setText(requestInfo.getLedger());

        JTextArea displayBalance = new JTextArea();
        displayBalance.setText(requestInfo.getBalance(requestInfo.publicKey));

        //JLABEL
        JLabel currentBalance = new JLabel("Current Balance:");

        JLabel enterPublicKey = new JLabel("Transfer Money: "); //finds user based on public key

        JLabel transactionHistory = new JLabel("Transaction History:");

        JLabel publicKey = new JLabel("Public Key: ");
        publicKey.setText("<html>"+ requestInfo.publicKey +"</html>");

        JLabel privateKey = new JLabel("Private Key: ");
        privateKey.setText("<html>"+ requestInfo.privateKey +"</html>");

        JTextField displayFunds = new JTextField(); //for the second window

        JLabel newWindowLabel = new JLabel("              Enter amount to transfer to " + requestInfo.getUserFromName(requestInfo.name));

        //PANELS AND GRIDS
        JPanel newPanel = new JPanel();
        newFrame.add(newPanel);
        GridLayout newGrid = new GridLayout(9, 1);
        newPanel.setLayout(newGrid);
        newPanel.add(currentBalance);
        newPanel.add(displayBalance);
        newPanel.add(enterPublicKey);
        newPanel.add(userButton);
        newPanel.add(transactionHistory);
        newPanel.add(displayTransactions);
        newPanel.add(publicKey);
        newPanel.add(privateKey);


        class userButtonListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e){
                //FIND USER PUBLIC KEY CODE HERE

                //OPEN NEW WINDOW
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

        class sendButtonListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    requestInfo.send();
                }
                catch(Exception e1) {
                    System.out.println("info didn't send. check button");
                    }
                amountFrame.dispose();
                System.out.println("Money successfully transferred");
                }
            }
        sendButton.addActionListener(new sendButtonListener());
        //makes everything appear on GUI window
        newPanel.revalidate();
    }
}
