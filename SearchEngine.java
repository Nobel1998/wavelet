
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class SearchEngine {

    public static class Handler implements HttpHandler {
        // List to store strings
        private List<String> stringsList = new ArrayList<>();

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            URI url = exchange.getRequestURI();
            String response;

            if (url.getPath().contains("/add")) {
                String[] parameters = url.getQuery().split("=");
                if (parameters.length > 1 && "s".equals(parameters[0])) {
                    stringsList.add(parameters[1]);
                    response = "String added successfully!";
                } else {
                    response = "Invalid parameters!";
                }
            } else if (url.getPath().contains("/search")) {
                String[] parameters = url.getQuery().split("=");
                List<String> results = new ArrayList<>();
                if (parameters.length > 1 && "s".equals(parameters[0])) {
                    String substring = parameters[1];
                    for (String str : stringsList) {
                        if (str.contains(substring)) {
                            results.add(str);
                        }
                    }
                    response = "Results: " + results.toString();
                } else {
                    response = "Invalid parameters!";
                }
            } else {
                response = "Unsupported path!";
            }

            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Please provide a port number as an argument.");
            return;
        }

        int port = Integer.parseInt(args[0]);
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new Handler());
        server.setExecutor(null);  // creates a default executor
        server.start();

        System.out.println("Server started on port " + port);
    }
}
