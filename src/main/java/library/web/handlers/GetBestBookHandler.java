package library.web.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import library.models.Book;
import library.services.LibraryService;
import library.services.LoanService;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class GetBestBookHandler implements HttpHandler {
    private final LibraryService libraryService;
    private final LoanService loanService;

    public GetBestBookHandler(LibraryService libraryService, LoanService loanService) {
        this.libraryService = libraryService;
        this.loanService = loanService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            Book bestBook = libraryService.findBestBook();
            String response;
            if (bestBook != null) {
                int loanCount = loanService.getTotalLoanCountForBook(bestBook);
                response = String.format("{\"title\":\"%s\", \"loanCount\": %d}",
                        bestBook.getTitle(), loanCount);
            } else {
                response = "{\"message\":\"No best book found.\"}";
            }

            byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
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