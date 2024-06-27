# Technical Analysis

## Contents

- [Introduction](#introduction)
- [Application Usage Instructions](#application-usage-instructions)
- [Structure](#structure)
   - [Structure Description](#structure-description)
- [Why was this specific structure chosen?](#why-was-this-specific-structure-chosen)
- [Data Flow](#data-flow)
   - [How do the handlers work?](#how-do-the-handlers-work)
   - [Handler Execution (Taking the `AddBookHandler` as an example)](#handler-execution-taking-the-addbookhandler-as-an-example)

## Introduction
This project is implemented as a Full-stack Web Application designed for managing a bookstore. Users can add books and readers, lend books, return books, and view various records and statistics. The application is accessible via a web browser at `http://localhost:8000`.

For this project, I researched the best practices used in large projects and I applied the principles of `Single Responsibility Principle` (SRP) and `Separation of Concerns`.

---

### Application Usage Instructions

#### Prerequisites
- Java Development Kit (JDK) version 8 or higher
- Basic understanding of terminal/command line usage

1. **Go to the Project Directory**:
   In the terminal, navigate to the root directory of the project (where the `src` folder is located).

2. **Compile the Java Code**:
   - Compile all java files

   ```sh
      javac -d out -sourcepath src -cp src $(find src/main/java -name "*.java")
    ```

3. **Run the Main Class**:

   ```sh
   java -cp out library.Main
   ```

4. **Access the Webpage**:
   Open a web browser and navigate to `http://localhost:8000`

----

### Structure

```plaintext
Bookstore/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── library/
│   │   │       ├── Main.java
│   │   │       ├── models/
│   │   │       │   ├── Book.java
│   │   │       │   ├── LoanEntry.java
│   │   │       │   ├── LoanJournal.java
│   │   │       │   ├── Reader.java
│   │   │       ├── utils/
│   │   │       │   └── StringUtils.java
│   │   │       ├── services/
│   │   │       │   ├── LibraryService.java
│   │   │       │   ├── LoanService.java
│   │   │       │   ├── ReaderService.java
│   │   │       ├── web/
│   │   │       │   ├── WebServer.java
│   │   │       │   ├── handlers/
│   │   │       │   │   ├── AddBookHandler.java
│   │   │       │   │   ├── AddReaderHandler.java
│   │   │       │   │   ├── LendBookHandler.java
│   │   │       │   │   ├── ReturnBookHandler.java
│   │   │       │   │   ├── GetAllBooksHandler.java
│   │   │       │   │   ├── GetAllReadersHandler.java
│   │   │       │   │   ├── GetLendedBooksHandler.java
│   │   │       │   │   ├── GetBookJournalHandler.java
│   │   │       │   │   ├── GetReaderJournalHandler.java
│   │   │       │   │   ├── GetBestReaderHandler.java
│   │   │       │   │   ├── GetBestBookHandler.java
│   │   │       │   │   ├── StaticFileHandler.java
│   ├── resources/
│   │   ├── static/
│   │   │   ├── index.html
│   │   │   ├── script.js
│   │   │   ├── styles.css
├── README.md
├── .gitignore
```

### Structure Description

- **models/**: Contains the data models (e.g., Book, LoanEntry, Reader).
- **utils/**: Contains utility methods for HTTP Requests to avoid duplicate code.
- **services/**: Contains services (e.g., LibraryService, LoanService, ReaderService) that handle the business logic.
- **web/**: Contains the web server and handlers for processing HTTP requests.
   - **handlers/**: Contains various HTTP handlers for CRUD operations.
- **resources/static/**: Contains the static files (HTML, CSS, JS).

- **Main.java**: The main entry point of the application.
- **WebServer.java**: Defines and starts the web server, registering the handlers.
- **README.md**: Contains information and instructions about the application.
- **.gitignore**: Git settings file, specifies which files/folders to ignore.

---

### Why was this specific structure chosen?

The choice of separating between `models` and `services` packages in the code structure aims to enhance the readability and maintainability of the application.

- The classes in the `models` directory represent the basic entities of the application, such as `Book`, `Reader`, `LoanEntry`, and `LoanJournal`. This means that the classes in this directory constitute the basic building blocks of the application. They serve as the blueprint for each specific object on which the application is built. Since these classes function as models, they do not contain the business logic of the application.

- In contrast, the business logic is implemented by the classes in the `services` package. The services in the `services` folder implement the business logic by handling functions such as adding books and readers, lending and returning books, and maintaining loan records. Additionally, these services are responsible for checking the uniqueness of each reader and book and performing essential validation and checking at multiple points in the code.

This approach offers multiple benefits, such as `encapsulation` and `abstraction`. The main benefit of this approach is that if the application is expanded with additional features in the future, we will only need to intervene in each specific service and not in the underlying models, which will remain stable.

---

### Data Flow

Since this structure functions as a Fullstack Web Application, a Web Server was created to serve the static files located in the `/resources` directory. Additionally, the server implements all the application's functionalities.

For this reason, handlers were created for the following functionalities:

1. AddBookHandler.java - Adds a new book to the library.
2. AddReaderHandler.java - Adds a new reader.
3. LendBookHandler.java - Lends a book to a reader.
4. ReturnBookHandler.java - Returns a book from a reader.
5. GetAllBooksHandler.java - Retrieves all books.
6. GetAllReadersHandler.java - Retrieves all readers.
7. GetLendedBooksHandler.java - Retrieves all lended books.
8. GetBookJournalHandler.java - Retrieves the journal of a specific book.
9. GetReaderJournalHandler.java - Retrieves the journal of a specific reader.
10. GetBestReaderHandler.java - Retrieves the best reader based on lending history.
11. GetBestBookHandler.java - Retrieves the best book based on lending history.
12. StaticFileHandler.java - Serves static files like HTML, CSS, and JS.

To make this work, communication between the frontend (index.html) and the backend code was established.
This communication was decided to be established by creating a `handlers` package. Within this package, each handler performs a specific function. Each handler has a name indicating the function it performs.

#### How do the handlers work?

To make the handlers work, i.e., to execute according to various events triggered by the user, the `script.js` was created. In this file, we associate the user's behavior with various Events (e.g., submit button), which run different functions defined in script.js and execute the handler.

```javascript
addBookForm.addEventListener('submit', async (event) => {
        event.preventDefault();
        const formData = new FormData(addBookForm);
        const params = new URLSearchParams(formData);
        const response = await fetch('/addBook', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: params.toString()
        });
        const result = await response.text();
        document.getElementById('response').innerText = result;
        addBookForm.reset();
    });
```

The above code is one such example. This particular function is found in `script.js`. As we observe, a fetch Request is made to the `/addBook` endpoint. We notice that the function in script.js collects all the data given by the user into the `formData` variable. It then passes this data to the handler. Similarly, data collection works for the other functionalities of the application.

Next, the `WebServer.java` calls the corresponding handler that has been associated with the specific endpoint.

```java
public static void startServer(String[] args) throws IOException {

        // ...

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

        // etc.
        // ...
    }
```

---

### What happens during the execution of the handlers?

Each handler executes the corresponding HTTP request and returns the response. During this execution, it checks the HTTP request and performs validation. Next, the respective methods we have specified in the `services` package, which contain the business logic of our application, are called within the handler. Essentially, the handler calls the service methods, passing them the data it has received from the user via the input tags from the application's graphical interface.

### Handler Execution (Taking the `AddBookHandler` as an example)

1. **Accepting HTTP Requests:**
   - The handler processes POST requests:
     ```java
     if ("POST".equals(exchange.getRequestMethod())) {
     ```

2. **Reading and Processing Data:**
   - The handler reads the request data, which comes from the GUI and is provided by the user through the input tags:
     ```java
     String formData = new String(exchange.getRequestBody().readAllBytes());
     Map<String, String> params = parseFormData(formData);
     ```

3. **Data Validation:**
   - Creating the `book` variable from the user-provided data:
     ```java
     Book book = new Book(params.get("ISBN"), params.get("title"), params.get("author"), params.get("genre"), params.get("location"));
     ```

4. **Calling Services:**
   - Calling the `addBook` method of the `LibraryService`:
     ```java
     boolean success = libraryService.addBook(book);
     ```

5. **Creating and Returning Response:**
   - Creating and sending the response:
     ```java
     String response = success ? "Book added successfully" : "Book already exists";
     exchange.sendResponseHeaders(success ? 200 : 409, response.length());
     OutputStream os = exchange.getResponseBody();
     os.write(response.getBytes());
     os.close();
     ```

6. **Error Handling:**
   - Handling and sending error response:
     ```java
     } catch (IllegalArgumentException e) {
         String response = "Invalid book data: " + e.getMessage();
         exchange.sendResponseHeaders(400, response.length());
         OutputStream os = exchange.getResponseBody();
         os.write(response.getBytes());
         os.close();
     }
     ```

The handler creates the `book` variable from the data it receives via the `parseFormData` method and uses it to call the `LibraryService` to add the book.

Similarly, other handlers work in the same manner.

---
