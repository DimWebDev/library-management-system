package library.web.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import library.models.Book;
import library.services.LibraryService;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.stream.Collectors;

public class GetAllBooksHandler implements HttpHandler {
    private final LibraryService libraryService;

    public GetAllBooksHandler(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            Collection<Book> books = libraryService.getAllBooks();
            String jsonResponse = books.stream()
                    .map(book -> String.format("{\"ISBN\":\"%s\",\"title\":\"%s\",\"author\":\"%s\",\"genre\":\"%s\",\"location\":\"%s\"}",
                            book.getISBN(), book.getTitle(), book.getAuthor(), book.getGenre(), book.getLocation()))
                    .collect(Collectors.joining(",", "[", "]"));

            byte[] responseBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(200, responseBytes.length);

            OutputStream os = exchange.getResponseBody();
            os.write(responseBytes);
            os.close();
        } else {
            String response = "Method Not Allowed";
            byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(405, responseBytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(responseBytes);
            os.close();
        }
    }
}