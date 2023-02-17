package com.CC.digitalLedger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

//runs on http://127.0.0.1 or localhost, which means your own computer
public class ServerRequest {
    public String domain;

    public ServerRequest(String domain) {
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

//    public String getTransactions(String publicKey) throws IOException, InterruptedException {
//        return getRequest("transactions/" + publicKey);
//    }

    public String getBalance(String publicKey) throws IOException, InterruptedException {
        return getRequest("balance/" + publicKey);
    } //USER INFO for public key and name

    public String getUsers() throws IOException, InterruptedException { //returns every single user
        return getRequest("users");
    }

    public String getUserFromKey(String publicKey) throws IOException, InterruptedException { //pick this one or next one
        return getRequest("users/" + publicKey);
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


    public static void main(String[] args) throws IOException, InterruptedException {
        //Initialize server with a running ngrok port. Make sure there is NO "/" at the end ;)
        ServerRequest server = new ServerRequest("https://e2ab-192-70-253-79.ngrok.io");
        System.out.println(server.getLedger());
    }
}
