package library.web.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import library.models.Reader;
import library.services.LibraryService;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import static library.utils.StringUtils.parseQuery;

public class AddReaderHandler implements HttpHandler {
    private final LibraryService libraryService;

    public AddReaderHandler(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            String formData = new String(exchange.getRequestBody().readAllBytes()); // These are the data of the HTTP POST Request. The formData come from addReaderForm.addEventListener in the resources/script.js.
            Map<String, String> params = parseQuery(formData);

            try {
                Reader reader = new Reader(params.get("TIN"), params.get("name"), Integer.parseInt(params.get("age")), params.get("category"));
                boolean success = libraryService.addReaderInLibrary(reader);

                String response = success ? "Reader added successfully" : "Reader already exists";
                exchange.sendResponseHeaders(success ? 200 : 409, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } catch (IllegalArgumentException e) {
                String response = "Invalid reader data: " + e.getMessage();
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