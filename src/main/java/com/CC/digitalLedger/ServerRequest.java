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

public class ServerRequest {
    public String domain;
    public Secret secret;

    //public Base64.Encoder encoder = Base64.getEncoder();
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
    }

    public String getUserFromName(String name) throws IOException, InterruptedException {
        return getRequest("users/name/" + name);
    }

    //POST REQUESTS
    public String send() throws IOException, InterruptedException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        HttpClient client = HttpClient.newHttpClient();
        String message = "You successfully sent " + "to ";
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, secret.privateKey());
        byte[] encryptedBytes = cipher.doFinal(message.getBytes());
        String encryptedMessage = new String(encryptedBytes);

        String req = String.format("{\"sender\": \"%s\", \"encrypted\": \"%2s\"}", secret.publicKeyAsString(), encryptedMessage);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(req))
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .uri(URI.create(domain + "/send"))
                .build();
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
