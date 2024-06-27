package library;

import java.io.IOException;
import library.web.WebServer;

public class Main {
    public static void main(String[] args) {
        try {
            WebServer.startServer(args);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}