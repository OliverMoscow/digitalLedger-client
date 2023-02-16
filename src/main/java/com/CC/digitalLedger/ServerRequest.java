package com.CC.digitalLedger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import java.util.zip.Deflater;

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

    public String getTransactions(String publicKey) throws IOException, InterruptedException {
        return getRequest("transactions/" + publicKey);
    }

    public String getBalance(String publicKey) throws IOException, InterruptedException {
        return getRequest("balance/" + publicKey);
    }

    public String getUsers() throws IOException, InterruptedException {
        return getRequest("users");
    }
    public String getUserFromKey(String publicKey) throws IOException, InterruptedException {
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

    public String newUser() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                //Set headers for post request. See google doc. https://www.baeldung.com/java-9-http-client.
                .headers("key1", "value1", "key2", "value2")
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

//    int port; //mailbox server looking for
//
//
//    public ServerRequest() throws IOException {
//        port = 1480; //http://127.0.0.1:1480
//        //default library in java, HTTP, as long as it lives, it will continue looking for messages in mailbox
//        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0); //socket is mailbox
//        //add event handler
//        server.createContext("/", new defaultHandler()); //new eventHandler
//        server.setExecutor(null);
//        server.start();
//    }
//    public class defaultHandler implements HttpHandler{ //event trigger when message in mailbox
//        @Override
//        public void handle(HttpExchange exchange) throws IOException {
//            String response = "</h> successfully connected to port!" + port + "</h>";
//            System.out.println(exchange.getRequestURI().getRawQuery());
//            //call whatever code here and it will return info
//            exchange.sendResponseHeaders(200, response.length()); //response codes, Code 200 is request is OK
//            OutputStream os = exchange.getResponseBody(); //write info into body of response
//            os.write(response.getBytes());
//        }
//    }

    //RESOURCES... good luck brutha
    //https://stackoverflow.com/questions/4474293/http-json-requests-in-java
    // JSON parser   https://stackoverflow.com/questions/2591098/how-to-parse-json-in-java
    //computer run service attached to internet



}
