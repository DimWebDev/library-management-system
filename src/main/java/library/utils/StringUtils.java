package library.utils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;


// We create this class to avoid duplicate code inside each handler
public class StringUtils {

    /**
     * Parses a URL-encoded query string or form data into a Map of key-value pairs.
     *
     * @param query The URL-encoded query string or form data.
     * @return A Map containing the key-value pairs.
     */
    public static Map<String, String> parseQuery(String query) {
        return Arrays.stream(query.split("&")) // Split key-value pairs to get all pairs: 👉👉👉 example ["ISBN=1234567890", "title=Java+Programming", "author=John+Doe", "genre=Technology", "location=Shelf+A"]
                .map(s -> s.split("=")) // Split each key-value pair into a key and value
                .collect(Collectors.toMap( // Create each key-value pair
                        e -> decode(e[0]), // Sets the key
                        e -> e.length > 1 ? decode(e[1]) : "" // Sets the value
                ));
    }

    /**
     * Decodes a URL-encoded string using UTF-8 encoding. It was considered important to be able to add Greek Characters
     *
     * @param value The URL-encoded string.
     * @return The decoded string.
     */
    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}