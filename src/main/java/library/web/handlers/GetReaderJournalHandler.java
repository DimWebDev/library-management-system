package library.web.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import library.models.LoanEntry;
import library.models.Reader;
import library.services.LibraryService;
import library.services.LoanService;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static library.utils.StringUtils.parseQuery;

public class GetReaderJournalHandler implements HttpHandler {
    private final LibraryService libraryService;
    private final LoanService loanService;

    public GetReaderJournalHandler(LibraryService libraryService, LoanService loanService) {
        this.libraryService = libraryService;
        this.loanService = loanService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            String query = exchange.getRequestURI().getQuery();
            Map<String, String> params = parseQuery(query);

            String TIN = params.get("TIN");

            if (TIN == null) {
                String response = "TIN parameter is missing";
                byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(400, responseBytes.length);
                OutputStream os = exchange.getResponseBody();
                os.write(responseBytes);
                os.close();
                return;
            }

            StringBuilder jsonResponse = new StringBuilder();
            jsonResponse.append("[");
            Reader reader = libraryService.getReaderByTIN(TIN);
            if (reader != null) {
                List<LoanEntry> readerEntries = loanService.getLoanJournalByReader(TIN);
                for (LoanEntry entry : readerEntries) {
                    jsonResponse.append("{")
                            .append("\"bookTitle\":\"").append(entry.getBook().getTitle()).append("\",")
                            .append("\"borrowDate\":\"").append(entry.getBorrowDate()).append("\",")
                            .append("\"returnDate\":\"").append(entry.getReturnDate() == null ? "Not Returned" : entry.getReturnDate()).append("\"")
                            .append("},");
                }
                if (jsonResponse.length() > 1) { // check if we have at least one entry
                    jsonResponse.setLength(jsonResponse.length() - 1); // Remove  splitting comma
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