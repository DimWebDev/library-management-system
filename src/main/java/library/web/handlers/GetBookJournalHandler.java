package library.web.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import library.models.Book;
import library.models.LoanEntry;
import library.services.LibraryService;
import library.services.LoanService;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static library.utils.StringUtils.parseQuery;

public class GetBookJournalHandler implements HttpHandler {
    private final LibraryService libraryService;
    private final LoanService loanService;

    public GetBookJournalHandler(LibraryService libraryService, LoanService loanService) {
        this.libraryService = libraryService;
        this.loanService = loanService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            String query = exchange.getRequestURI().getQuery();
            Map<String, String> params = parseQuery(query);

            String ISBN = params.get("ISBN");

            if (ISBN == null) {
                String response = "ISBN parameter is missing";
                byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(400, responseBytes.length);
                OutputStream os = exchange.getResponseBody();
                os.write(responseBytes);
                os.close();
                return;
            }

            StringBuilder jsonResponse = new StringBuilder();
            jsonResponse.append("[");
            Book book = libraryService.getBookByISBN(ISBN); // get the Book by using ISBN
            if (book != null) {
                List<LoanEntry> bookEntries = loanService.getLoanJournalByBook(ISBN); // Get all loans entries of the given book
                for (LoanEntry entry : bookEntries) {
                    jsonResponse.append("{")
                            .append("\"readerName\":\"").append(entry.getReader().getName()).append("\",")
                            .append("\"borrowDate\":\"").append(entry.getBorrowDate()).append("\",")
                            .append("\"returnDate\":\"").append(entry.getReturnDate() == null ? "Not Returned" : entry.getReturnDate()).append("\"")
                            .append("},");
                }
                if (jsonResponse.length() > 1) { // check if we have at least one loan entry
                    jsonResponse.setLength(jsonResponse.length() - 1); // remove comma after entry loan
                }
            }
            jsonResponse.append("]");

            byte[] responseBytes = jsonResponse.toString().getBytes(StandardCharsets.UTF_8);
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