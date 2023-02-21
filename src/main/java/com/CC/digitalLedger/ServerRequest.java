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


    public Base64.Encoder encoder = Base64.getEncoder();


    public ServerRequest(String domain) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        this.domain = domain;
        this.secret = Backup.load();
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
        return getRequest("balance/" + secret.publicKeyAsString());
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
    public String send(String amount, String receiver) throws IOException, InterruptedException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        // Create HTTP client
        HttpClient client = HttpClient.newHttpClient();

        // Create message
        String message = String.format("{\"receiver\":\"%s\", \"amount\": \"2%f\"}", receiver, amount).replace((char) 4, '\\');

        // Encrypt message
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, secret.privateKey());
        byte[] encryptedBytes = cipher.doFinal(message.getBytes());
        String encryptedMessage = Base64.getEncoder().encodeToString(encryptedBytes);

        // Create request
        String req = String.format("{\"sender\": \"%s\", \"encrypted\": \"2%s\"}", secret.publicKeyAsString(), encryptedMessage);
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

    public String instantiateUser(String name) throws IOException, InterruptedException {
        String req = String.format("{\"name\": \"%s\", \"publicKey\": \"%2s\"}", name, secret.publicKeyAsString());
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
}
