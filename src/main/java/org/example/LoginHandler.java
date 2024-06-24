package org.example;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class LoginHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {

            InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            br.close();
            String requestBody = sb.toString().trim();


            Map<String, String> params = parseQuery(requestBody);

            String login = params.get("login");
            String password = params.get("password");


            if ("user1".equals(login) && "password1".equals(password)) {
                // Generate JWT token
                String token = Utils.generateToken(login);
                exchange.sendResponseHeaders(200, token.length());
                OutputStream os = exchange.getResponseBody();
                os.write(token.getBytes());
                os.close();
            } else {
                exchange.sendResponseHeaders(401, 0);
                exchange.close();
            }
        } else {
            exchange.sendResponseHeaders(405, 0); // Method Not Allowed
            exchange.close();
        }
    }


    private Map<String, String> parseQuery(String query) {
        Map<String, String> params = new HashMap<>();
        if (query != null && !query.isEmpty()) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                if (idx > 0) {
                    String key = pair.substring(0, idx);
                    String value = pair.substring(idx + 1);
                    params.put(key, value);
                }
            }
        }
        return params;
    }
}
