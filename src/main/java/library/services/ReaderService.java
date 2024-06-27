package library.services;

import library.models.Reader;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ReaderService {
    private Map<String, Reader> readers; // use a Map to in order to use TIN as key, because a Map stores data in key-value pairs

    public ReaderService() {
        this.readers = new HashMap<>();
    }

    public void validateReader(Reader reader) {
        if (reader.getTIN() == null || reader.getTIN().isEmpty()) {
            throw new IllegalArgumentException("TIN cannot be null or empty.");
        }
        if (reader.getName() == null || reader.getName().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        if (reader.getAge() <= 0) {
            throw new IllegalArgumentException("Age must be greater than zero.");
        }
        if (reader.getCategory() == null || (!reader.getCategory().equals("Student") && !reader.getCategory().equals("Other"))) {
            throw new IllegalArgumentException("Category must be either 'Student' or 'Other'.");
        }
    }

    public Reader getReaderByTIN(String TIN) {
        return readers.get(TIN); // use get method of the Map interface to retrieve the value of a reader by using TIN as the key
    }

    public boolean addReader(Reader reader) {
        validateReader(reader);
        if (readers.containsKey(reader.getTIN())) {
            throw new IllegalArgumentException("Reader with TIN " + reader.getTIN() + " already exists.");
        }
        readers.put(reader.getTIN(), reader);
        System.out.println("Reader added: " + reader.getName());
        return true;
    }

    public Collection<Reader> getAllReaders() {
        return readers.values();
    }
}