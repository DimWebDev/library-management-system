package library.services;

import library.models.*;

import java.util.*;

public class LibraryService {
    private Map<String, Book> books; // use a Map to in order to use ISBN as key, because a Map stores data in key-value pairs
    private ReaderService readerService;
    private LoanService loanService;

    public LibraryService(LoanService loanService, ReaderService readerService) {
        this.books = new HashMap<>(); // use a Map to in order to use ISBN as key, because a Map stores data in key-value pairs. We are using this later when we search for books with ISBN as the key
        this.readerService = readerService;
        this.loanService = loanService;
    }

    public boolean addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Cannot add a null book.");
        }
        if (book.getISBN() == null || book.getISBN().isEmpty()) {
            throw new IllegalArgumentException("Book must have a valid ISBN.");
        }
        if (books.containsKey(book.getISBN())) {
            System.out.println("Book with ISBN " + book.getISBN() + " already exists.");
            return false;
        } else {
            books.put(book.getISBN(), book);
            System.out.println("Book added: " + book.getTitle());
            return true;
        }
    }

    public boolean addReaderInLibrary(Reader reader) {
        try {
            return readerService.addReader(reader);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean lendBook(String ISBN, String TIN) {
        Book book = books.get(ISBN); // use get method of the Map interface to retrieve a value by using ISBN as the key
        Reader reader = readerService.getReaderByTIN(TIN);

        if (book == null) {
            System.out.println("Book with ISBN " + ISBN + " not found.");
            return false;
        }

        if (reader == null) {
            System.out.println("Reader with TIN " + TIN + " not found.");
            return false;
        }

        for (LoanEntry entry : loanService.getLoanJournalByBook(ISBN)) {
            if (entry.getReturnDate() == null) {
                System.out.println("Book " + book.getTitle() + " is already on loan.");
                return false;
            }
        }

        LoanEntry entry = new LoanEntry(reader, book, java.time.LocalDate.now());
        loanService.addLoanEntry(entry);
        System.out.println("Book " + book.getTitle() + " lent to " + reader.getName() + ".");
        return true;
    }

    public boolean returnBook(String ISBN, String TIN) {
        Book book = books.get(ISBN);
        Reader reader = readerService.getReaderByTIN(TIN);

        if (book == null) {
            System.out.println("Book with ISBN " + ISBN + " not found.");
            return false;
        }

        if (reader == null) {
            System.out.println("Reader with TIN " + TIN + " not found.");
            return false;
        }

        for (LoanEntry entry : loanService.getLoanJournalByBook(ISBN)) {
            if (entry.getReader().equals(reader) && entry.getReturnDate() == null) {
                loanService.closeLoanEntry(entry);
                System.out.println("Book " + book.getTitle() + " returned by " + reader.getName() + ".");
                return true;
            }
        }

        System.out.println("No active loan found for book " + book.getTitle() + " by " + reader.getName() + ".");
        return false;
    }


    public Reader findBestReader() {
        Map<Reader, Integer> readerLoanCount = new HashMap<>();

        for (LoanEntry entry : loanService.getAllLoans()) {
            Reader reader = entry.getReader();
            int totalLoanCount = loanService.getTotalLoanCountForReader(reader);
            readerLoanCount.put(reader, totalLoanCount);
        }

        return readerLoanCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public Book findBestBook() {
        Map<Book, Integer> bookLoanCount = new HashMap<>();

        for (LoanEntry entry : loanService.getAllLoans()) {
            Book book = entry.getBook();
            int totalLoanCount = loanService.getTotalLoanCountForBook(book);
            bookLoanCount.put(book, totalLoanCount);
        }

        return bookLoanCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }


    // 👉 👉 👉 Methods to display the core functionalities of the Application and check the successful addition of Readers and Books

    // method to fetch all books
    public Collection<Book> getAllBooks() {
        return books.values();
    }

    // method to fetch all readers
    public Collection<Reader> getAllReaders() {
        return readerService.getAllReaders();
    }

    // method to get fetch by ISBN as the unique identifier
    public Book getBookByISBN(String ISBN) {
        return books.get(ISBN);
    }

    // method to fetch reader by TIN as the unique identifier
    public Reader getReaderByTIN(String TIN) {
        return readerService.getReaderByTIN(TIN);
    }
}