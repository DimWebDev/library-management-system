package library.web.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import library.models.LoanEntry;
import library.services.LoanService;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.stream.Collectors;

public class GetLendedBooksHandler implements HttpHandler {
    private final LoanService loanService;

    public GetLendedBooksHandler(LoanService loanService) {
        this.loanService = loanService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            Collection<LoanEntry> lendedBooks = loanService.getActiveLoans();
            String jsonResponse = lendedBooks.stream()
                    .map(loanEntry -> String.format("{\"bookTitle\":\"%s\",\"readerName\":\"%s\"}",
                            loanEntry.getBook().getTitle(), loanEntry.getReader().getName()))
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