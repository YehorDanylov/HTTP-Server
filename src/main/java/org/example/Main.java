package org.example;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    private static final int PORT = 8000;

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // Create context for the root path
        server.createContext("/", new RootHandler());

        // Create context for /login endpoint
        server.createContext("/login", new LoginHandler());

        // Create context for /api/good endpoint
        server.createContext("/api/good", new GoodsHandler());

        server.setExecutor(null); // creates a default executor

        server.start();
        System.out.println("Server started on port " + PORT);
    }

}
