package library.services;

import library.models.*;

import java.util.ArrayList;
import java.util.List;

public class LoanService {
    private LoanJournal loanJournal;

    public LoanService() {
        this.loanJournal = new LoanJournal();
    }

    public void addLoanEntry(LoanEntry entry) {
        loanJournal.addEntry(entry);
        System.out.println("Loan entry added for book: " + entry.getBook().getTitle() + " by reader: " + entry.getReader().getName());
    }

    public void closeLoanEntry(LoanEntry entry) {
        entry.setReturnDate(java.time.LocalDate.now());
        System.out.println("Loan entry closed for book: " + entry.getBook().getTitle() + " by reader: " + entry.getReader().getName());
    }

    public List<LoanEntry> getAllLoans() {
        return loanJournal.getAllEntries();
    }

    public int getTotalLoanCountForReader(Reader reader) {
        int count = 0;
        for (LoanEntry entry : loanJournal.getAllEntries()) {
            if (entry.getReader().equals(reader)) {
                count++;
            }
        }
        return count;
    }

    public int getTotalLoanCountForBook(Book book) {
        int count = 0;
        for (LoanEntry entry : loanJournal.getAllEntries()) {
            if (entry.getBook().equals(book)) {
                count++;
            }
        }
        return count;
    }

    public List<LoanEntry> getLoanJournalByBook(String ISBN) {
        List<LoanEntry> bookEntries = new ArrayList<>();
        for (LoanEntry entry : loanJournal.getAllEntries()) {
            if (entry.getBook().getISBN().equals(ISBN)) {
                bookEntries.add(entry);
            }
        }
        return bookEntries;
    }

    public List<LoanEntry> getLoanJournalByReader(String TIN) {
        List<LoanEntry> readerEntries = new ArrayList<>();
        for (LoanEntry entry : loanJournal.getAllEntries()) {
            if (entry.getReader().getTIN().equals(TIN)) {
                readerEntries.add(entry);
            }
        }
        return readerEntries;
    }

    public List<LoanEntry> getActiveLoans() {
        List<LoanEntry> activeLoans = new ArrayList<>();
        for (LoanEntry entry : loanJournal.getAllEntries()) {
            if (entry.getReturnDate() == null) {
                activeLoans.add(entry);
            }
        }
        return activeLoans;
    }
}



