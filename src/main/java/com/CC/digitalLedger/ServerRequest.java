package com.CC.digitalLedger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Map;

import java.util.zip.Deflater;

//runs on http://127.0.0.1 or localhost, which means your own computer
public class ServerRequest {
    int port; //mailbox server looking for

    //private key in a file somewhere

    public ServerRequest() throws IOException {
        port = 1480; //http://127.0.0.1:1480
        //default library in java, HTTP, as long as it lives, it will continue looking for messages in mailbox
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0); //socket is mailbox
        //add event handler
        server.createContext("/", new defaultHandler()); //new eventHandler
        server.setExecutor(null);
        server.start();
    }
    public class defaultHandler implements HttpHandler{ //event trigger when message in mailbox
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "</h> successfully connected to port!" + port + "</h>";
            System.out.println(exchange.getRequestURI().getRawQuery());
            //call whatever code here and it will return info
            exchange.sendResponseHeaders(200, response.length()); //response codes, Code 200 is request is OK
            OutputStream os = exchange.getResponseBody(); //write info into body of response
            os.write(response.getBytes());
        }
    }

    //RESOURCES... good luck brutha
    //https://stackoverflow.com/questions/4474293/http-json-requests-in-java
    // JSON parser   https://stackoverflow.com/questions/2591098/how-to-parse-json-in-java
    //computer run service attached to internet



}
