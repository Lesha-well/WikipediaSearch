package org.example.wikifinder.exception;

public class WikipediaResponseException extends Exception {

    public WikipediaResponseException(String message) {
        super(message);
    }

    public WikipediaResponseException(String message, Throwable cause) {
        super(message, cause);
    }
}