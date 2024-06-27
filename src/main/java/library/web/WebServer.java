package library.web;

import com.sun.net.httpserver.HttpServer; // import this to instantiate a web server
import library.web.handlers.*; // import all the handlers to enable HTTP communication

// import services
import library.services.LibraryService;
import library.services.LoanService;
import library.services.ReaderService;

import java.io.IOException;
import java.net.InetSocketAddress;

public class WebServer {

    private static LoanService loanService = new LoanService();
    private static ReaderService readerService = new ReaderService();
    private static LibraryService libraryService = new LibraryService(loanService, readerService);

    // method to start the server and associate handlers with each endpoint
    public static void startServer(String[] args) throws IOException {
        // Create an HTTP server listening on port 8000
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        // Serve static files from the /static directory when the server starts
        server.createContext("/", new StaticFileHandler());

        // Register the handlers for CRUD operations of our Application
        // the createContext maps each handler to the corresponding endpoint
        server.createContext("/addBook", new AddBookHandler(libraryService));
        server.createContext("/addReader", new AddReaderHandler(libraryService));
        server.createContext("/lendBook", new LendBookHandler(libraryService));
        server.createContext("/returnBook", new ReturnBookHandler(libraryService));
        server.createContext("/getAllBooks", new GetAllBooksHandler(libraryService));
        server.createContext("/getAllReaders", new GetAllReadersHandler(libraryService));
        server.createContext("/lendedBooks", new GetLendedBooksHandler(loanService));
        server.createContext("/getBookJournal", new GetBookJournalHandler(libraryService, loanService));
        server.createContext("/getReaderJournal", new GetReaderJournalHandler(libraryService, loanService));
        server.createContext("/bestReader", new GetBestReaderHandler(libraryService, loanService));
        server.createContext("/bestBook", new GetBestBookHandler(libraryService, loanService));

        // Start the server
        server.setExecutor(null); // use a default executor
        server.start();
        System.out.println("Server started on port 8000");
        System.out.println("Access the Application at => http://localhost:8000/");
    }
}