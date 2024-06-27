// This class contains Entries of the Loans of Books. It works like a registry of all book lending and returning operations.
package library.models;

import java.util.ArrayList;
import java.util.List;

public class LoanJournal {
    private List<LoanEntry> entries;

    public LoanJournal() {
        this.entries = new ArrayList<>();
    }

    public void addEntry(LoanEntry entry) {
        this.entries.add(entry);
    }

    public List<LoanEntry> getAllEntries() {
        return new ArrayList<>(entries);
    }
}