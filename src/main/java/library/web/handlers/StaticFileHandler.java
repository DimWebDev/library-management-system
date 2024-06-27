package library.web.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StaticFileHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if ("/".equals(path)) {
            path = "/index.html";
        }

        if (!path.startsWith("/")) {
            path = "/" + path;
        }

        Path filePath = Paths.get("src/main/resources/static" + path);
        byte[] response;
        try {
            response = Files.readAllBytes(filePath); // read all the contents of the static file from the given path
            String contentType = getContentType(filePath); // determine content type based on the filepath extension
            exchange.getResponseHeaders().set("Content-Type", contentType);
            exchange.sendResponseHeaders(200, response.length);
        } catch (IOException e) {
            String errorMessage = "404 Not Found: " + filePath;
            System.err.println(errorMessage);
            response = errorMessage.getBytes();
            exchange.sendResponseHeaders(404, response.length);
        }

        OutputStream os = exchange.getResponseBody(); // create output stream to serve the static files as a response
        os.write(response); // the response byte [] contains all the data from the static files
        os.close();
    }

    private String getContentType(Path filePath) {
        String path = filePath.toString();
        if (path.endsWith(".html")) {
            return "text/html";
        } else if (path.endsWith(".css")) {
            return "text/css";
        } else if (path.endsWith(".js")) {
            return "application/javascript";
        } else {
            return "application/octet-stream";
        }
    }
}