import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Map;

public class StringServer {

    private static String message = "";

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/add-message", new MessageHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("StringServer started at http://localhost:8000");
    }

    static class MessageHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            Map<String, String> parameters = parseQuery(query);
            String newMessage = parameters.get("s");
            message += newMessage + "\n";

            byte[] response = message.getBytes();
            exchange.sendResponseHeaders(200, response.length);
            OutputStream os = exchange.getResponseBody();
            os.write(response);
            os.close();
        }

        private Map<String, String> parseQuery(String query) {
            // Simple query parser
            return Map.of(query.split("=")[0], query.split("=")[1]);
        }
    }
}
