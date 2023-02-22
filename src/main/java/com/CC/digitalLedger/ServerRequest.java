package com.CC.digitalLedger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;


//runs on http://127.0.0.1 or localhost, which means your own computer
public class ServerRequest {
    public String domain;
    public Secret secret;
    public String username;

    public Base64.Encoder encoder = Base64.getEncoder();


    public ServerRequest(String domain, Backup backup) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        this.domain = domain;
        System.out.println("Running on port: " + domain);
        this.secret = backup.secret;
        this.username = backup.name;
    }

    //A get request with an endpoint. Make sure there is NO "/" at the beginning of endpoint.
    private String getRequest(String endpoint) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(domain + "/" + endpoint))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }


    public String getLedger() throws IOException, InterruptedException {
        return getRequest("ledger");
    }

    public String getBalance() throws IOException, InterruptedException {
        return getRequest("balance/" + username);
    } //USER INFO for public key and name


    public String getUsers() throws IOException, InterruptedException { //returns every single user
        return getRequest("users");
    }


    public String currentUser() throws IOException, InterruptedException { //pick this one or next one
        return getRequest("users/" + secret.publicKeyAsString());
    }


    public String getUserFromName(String name) throws IOException, InterruptedException {
        return getRequest("users/name/" + name);
    }



    //POST REQUESTS
    public String send(Double amount, String receiver) throws IOException, InterruptedException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {


        // Create HTTP client
        HttpClient client = HttpClient.newHttpClient();

        // Create message
        String message = String.format("{\"receiver\":\"%s\", \"amount\": %f}", receiver, amount);

        // Encrypt message
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, secret.privateKey());
        byte[] encryptedBytes = cipher.doFinal(message.getBytes());
        String encryptedMessage = Base64.getEncoder().encodeToString(encryptedBytes);

        if (username == "") {
            return null;
        }

        // Create request
        String req = String.format("{\"sender\": \"%s\",\"senderName\": \"%s\", \"encrypted\": \"%s\"}", secret.publicKeyAsString(),username, encryptedMessage);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(domain + "/send"))
                .header("Content-Type", "application/json")
                .header("accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(req))
                .build();

        // Send request and get response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public String instantiateUser() throws IOException, InterruptedException {
        String req = String.format("{\"name\": \"%s\", \"publicKey\": \"%2s\"}", username, secret.publicKeyAsString());
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(req))
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .uri(URI.create(domain + "/newUser"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

//    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InterruptedException {
//        ServerRequest server = new ServerRequest("https://8ce2-192-70-253-79.ngrok.io");
//        server.send(100.00, "Vincent");
//    }
}
