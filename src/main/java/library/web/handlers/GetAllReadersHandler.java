package library.web.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import library.models.Reader;
import library.services.LibraryService;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.stream.Collectors;

public class GetAllReadersHandler implements HttpHandler {
    private final LibraryService libraryService;

    public GetAllReadersHandler(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            Collection<Reader> readers = libraryService.getAllReaders();
            String jsonResponse = readers.stream()
                    .map(reader -> String.format("{\"TIN\":\"%s\",\"name\":\"%s\",\"age\":\"%d\",\"category\":\"%s\"}",
                            reader.getTIN(), reader.getName(), reader.getAge(), reader.getCategory()))
                    .collect(Collectors.joining(",", "[", "]"));

            exchange.getResponseHeaders().set("Content-Type", "application/json;charset=utf-8");
            byte[] responseBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
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