package library.web.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import library.models.Book;
import library.services.LibraryService;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import static library.utils.StringUtils.parseQuery;

public class AddBookHandler implements HttpHandler {
    private final LibraryService libraryService;

    public AddBookHandler(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            String formData = new String(exchange.getRequestBody().readAllBytes()); // These are the data of the HTTP POST Request. The formData come from addBookForm.addEventListener in the resources/script.js.
            Map<String, String> params = parseQuery(formData);  // create a map with the FormData as key-value pairs

            try {
                Book book = new Book(params.get("ISBN"), params.get("title"), params.get("author"), params.get("genre"), params.get("location")); // use the data from the POST request to add the book
                boolean success = libraryService.addBook(book);

                String response = success ? "Book added successfully" : "Book already exists";
                exchange.sendResponseHeaders(success ? 200 : 409, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } catch (IllegalArgumentException e) {
                String response = "Invalid book data: " + e.getMessage();
                exchange.sendResponseHeaders(400, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        } else {
            String response = "Method Not Allowed";
            exchange.sendResponseHeaders(405, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}