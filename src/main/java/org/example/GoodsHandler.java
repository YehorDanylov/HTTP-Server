package org.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class GoodsHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String endpoint = exchange.getRequestURI().getPath();


        String token = exchange.getRequestHeaders().getFirst("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7); // Remove "Bearer " prefix
            if (!Utils.verifyToken(jwtToken)) {
                exchange.sendResponseHeaders(403, 0); // Forbidden
                exchange.close();
                return;
            }
        } else {
            exchange.sendResponseHeaders(401, 0); // Unauthorized
            exchange.close();
            return;
        }


        switch (exchange.getRequestMethod()) {
            case "GET":
                handleGet(exchange, endpoint);
                break;
            case "PUT":
                handlePut(exchange);
                break;
            case "POST":
                handlePost(exchange);
                break;
            case "DELETE":
                handleDelete(exchange);
                break;
            default:
                exchange.sendResponseHeaders(405, 0); // Method Not Allowed
                exchange.close();
        }
    }

    private void handleGet(HttpExchange exchange, String endpoint) throws IOException {
        if (endpoint.equals("/api/good")) {

            String jsonResponse = "[{\"id\": \"1\", \"name\": \"Product A\", \"price\": 99.99}, " +
                    "{\"id\": \"2\", \"name\": \"Product B\", \"price\": 149.99}]";
            exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();
        } else {

            String id = endpoint.substring("/api/good/".length());

            String jsonResponse = "{\"id\": \"" + id + "\", \"name\": \"Good Name\"}";
            exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();
        }
    }

    private void handlePut(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(201, 0); // Created
        exchange.close();
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String requestBody = Utils.convertStreamToString(exchange.getRequestBody());
        System.out.println("Received POST request body: " + requestBody);

        // Respond with appropriate status
        exchange.sendResponseHeaders(204, 0); // No Content
        exchange.close();
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        // Handle DELETE request
        // Process request and respond
        exchange.sendResponseHeaders(204, 0); // No Content
        exchange.close();
    }
}
