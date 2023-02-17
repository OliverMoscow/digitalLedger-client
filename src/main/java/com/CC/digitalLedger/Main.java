package com.CC.digitalLedger;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        //Initialize server with a running ngrok port. Make sure there is NO "/" at the end ;)
        ServerRequest server = new ServerRequest("https://e2ab-192-70-253-79.ngrok.io");
        //System.out.println(server.getLedger());

        Window thisGUI = new Window();
    }
}