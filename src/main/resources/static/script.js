document.addEventListener('DOMContentLoaded', () => {
    const addBookForm = document.getElementById('add-book-form');
    const addReaderForm = document.getElementById('add-reader-form');
    const lendBookForm = document.getElementById('lend-book-form');
    const returnBookForm = document.getElementById('return-book-form');
    const fetchBooksButton = document.getElementById('fetch-books');
    const fetchReadersButton = document.getElementById('fetch-readers');
    const fetchLendedBooksButton = document.getElementById('fetch-lended-books');
    const booksList = document.getElementById('books-list');
    const readersList = document.getElementById('readers-list');
    const lendedBooksList = document.getElementById('lendedBooksList');

    const getBookJournalButton = document.getElementById('get-book-journal');
    const getReaderJournalButton = document.getElementById('get-reader-journal');
    const bookJournalList = document.getElementById('book-journal-list');
    const readerJournalList = document.getElementById('reader-journal-list');
    const fetchBestReaderButton = document.getElementById('fetch-best-reader');
    const fetchBestBookButton = document.getElementById('fetch-best-book');
    const bestReaderDiv = document.getElementById('best-reader');
    const bestBookDiv = document.getElementById('best-book');

    addBookForm.addEventListener('submit', async (event) => {
        event.preventDefault();
        const formData = new FormData(addBookForm); // collects all the data from the fields of the form
        const params = new URLSearchParams(formData); // formats the form data as a URL-encoded string (e.g., key1=value1&key2=value2).
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

    addReaderForm.addEventListener('submit', async (event) => {
        event.preventDefault();
        const formData = new FormData(addReaderForm);
        const params = new URLSearchParams(formData);
        const response = await fetch('/addReader', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: params.toString()
        });
        const result = await response.text();
        document.getElementById('response-reader').innerText = result;
        addReaderForm.reset();
    });

    lendBookForm.addEventListener('submit', async (event) => {
        event.preventDefault();
        const formData = new FormData(lendBookForm);
        const params = new URLSearchParams(formData);
        const response = await fetch('/lendBook', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: params.toString()
        });
        const result = await response.text();
        document.getElementById('response-lend').innerText = result;
        lendBookForm.reset();
    });

    returnBookForm.addEventListener('submit', async (event) => {
        event.preventDefault();
        const formData = new FormData(returnBookForm);
        const params = new URLSearchParams(formData);
        const response = await fetch('/returnBook', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: params.toString()
        });
        const result = await response.text();
        document.getElementById('response-return').innerText = result;
        returnBookForm.reset();
    });

    fetchBooksButton.addEventListener('click', async () => {
        const response = await fetch('/getAllBooks', {
            method: 'GET',
        });
        const books = await response.json();
        booksList.innerHTML = '';
        books.forEach(book => {
            const li = document.createElement('li');
            li.textContent = `${book.title} by ${book.author}, Genre: ${book.genre}, Location: ${book.location} (ISBN: ${book.ISBN})`;
            booksList.appendChild(li);
        });
    });

    fetchReadersButton.addEventListener('click', async () => {
        const response = await fetch('/getAllReaders', {
            method: 'GET',
        });
        const readers = await response.json();
        readersList.innerHTML = '';
        readers.forEach(reader => {
            const li = document.createElement('li');
            li.textContent = `${reader.name} (TIN: ${reader.TIN}, Age: ${reader.age}, Category: ${reader.category})`;
            readersList.appendChild(li);
        });
    });

    fetchLendedBooksButton.addEventListener('click', async () => {
        const response = await fetch('/lendedBooks', {
            method: 'GET',
        });
        const lendedBooks = await response.json();
        lendedBooksList.innerHTML = '';
        lendedBooks.forEach(loanEntry => {
            const li = document.createElement('li');
            li.textContent = `Book: ${loanEntry.bookTitle}, Borrowed by: ${loanEntry.readerName}`;
            lendedBooksList.appendChild(li);
        });
    });

    getBookJournalButton.addEventListener('click', async () => {
        const isbn = prompt("Enter ISBN to fetch loan journal:");
        if (!isbn) return;
        const response = await fetch(`/getBookJournal?ISBN=${isbn}`, {
            method: 'GET',
        });
        const journalEntries = await response.json();
        bookJournalList.innerHTML = '';
        if (journalEntries.length === 0) {
            const li = document.createElement('li');
            li.textContent = `No loan entries found for ISBN: ${isbn}`;
            bookJournalList.appendChild(li);
        } else {
            journalEntries.forEach(entry => {
                const li = document.createElement('li');
                li.textContent = `Reader: ${entry.readerName}, Borrow Date: ${entry.borrowDate}, Return Date: ${entry.returnDate}`;
                bookJournalList.appendChild(li);
            });
        }
    });

    getReaderJournalButton.addEventListener('click', async () => {
        const tin = prompt("Enter TIN to fetch loan journal:");
        if (!tin) return;
        const response = await fetch(`/getReaderJournal?TIN=${tin}`, {
            method: 'GET',
        });
        const journalEntries = await response.json();
        readerJournalList.innerHTML = '';
        if (journalEntries.length === 0) {
            const li = document.createElement('li');
            li.textContent = `No loan entries found for TIN: ${tin}`;
            readerJournalList.appendChild(li);
        } else {
            journalEntries.forEach(entry => {
                const li = document.createElement('li');
                li.textContent = `Book: ${entry.bookTitle}, Borrow Date: ${entry.borrowDate}, Return Date: ${entry.returnDate}`;
                readerJournalList.appendChild(li);
            });
        }
    });

    fetchBestReaderButton.addEventListener('click', async () => {
        const response = await fetch('/bestReader', {
            method: 'GET'
        });
        if (response.ok) {
            const bestReader = await response.json();
            if (bestReader.message) {
                bestReaderDiv.textContent = bestReader.message;
            } else {
                bestReaderDiv.textContent = `Best Reader: ${bestReader.name} with ${bestReader.loanCount} book(s) read.`;
            }
        } else {
            bestReaderDiv.textContent = 'Failed to fetch best reader.';
        }
    });

    fetchBestBookButton.addEventListener('click', async () => {
        const response = await fetch('/bestBook', {
            method: 'GET'
        });
        if (response.ok) {
            const bestBook = await response.json();
            if (bestBook.message) {
                bestBookDiv.textContent = bestBook.message;
            } else {
                bestBookDiv.textContent = `Best Book: ${bestBook.title} with ${bestBook.loanCount} loan(s).`;
            }
        } else {
            bestBookDiv.textContent = 'Failed to fetch best book.';
        }
    });
});