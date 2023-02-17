package com.CC.digitalLedger;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.*;
import java.util.Base64;


//runs on http://127.0.0.1 or localhost, which means your own computer
public class ServerRequest {
    public String domain;
    public KeyPair keyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
    public PublicKey publicKey = keyPair.getPublic();
    public PrivateKey privateKey = keyPair.getPrivate();

    public String name;


    public Base64.Encoder encoder = Base64.getEncoder();


    public ServerRequest(String domain) throws NoSuchAlgorithmException {
        this.domain = domain;


    }


    //private key in a file somewhere


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

    public String getBalance(PublicKey publicKey) throws IOException, InterruptedException {
        String publicKeyStr = encoder.encodeToString(publicKey.getEncoded());
        return getRequest("balance/" + publicKeyStr);
    } //USER INFO for public key and name


    public String getUsers() throws IOException, InterruptedException { //returns every single user
        return getRequest("users");
    }


    public String getUserFromKey(PublicKey publicKey) throws IOException, InterruptedException { //pick this one or next one
        String publicKeyStr = encoder.encodeToString(publicKey.getEncoded());
        return getRequest("users/" + publicKeyStr);
    }


    public String getUserFromName(String name) throws IOException, InterruptedException {
        return getRequest("users/" + name);
    }


    //POST REQUESTS
    public String send() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                //INCOMPLETE: Set headers for post request. See google doc. https://www.baeldung.com/java-9-http-client.
                .headers("key1", "value1", "key2", "value2")
                .uri(URI.create(domain + "/send"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }


    public String newUser(String name, String publicKey) throws IOException, InterruptedException {
        String req = String.format("{name: %s, publicKey: %2s", name, publicKey);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(req))
                .header("accept", "application/json")
                .uri(URI.create(domain + "/newUser"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
