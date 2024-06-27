package library.web.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import library.services.LibraryService;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import static library.utils.StringUtils.parseQuery;

public class LendBookHandler implements HttpHandler {
    private final LibraryService libraryService;

    public LendBookHandler(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            String formData = new String(exchange.getRequestBody().readAllBytes());
            Map<String, String> params = parseQuery(formData);

            String ISBN = params.get("ISBN");
            String TIN = params.get("TIN");

            boolean success = libraryService.lendBook(ISBN, TIN);

            String response;
            if (success) {
                response = "Book lent successfully";
                exchange.sendResponseHeaders(200, response.length());
            } else {
                response = "Failed to lend book";
                exchange.sendResponseHeaders(400, response.length());
            }

            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            String response = "Method Not Allowed";
            exchange.sendResponseHeaders(405, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

}