package org.example.lbjavaspring.exception;

public class ConfigurationException extends RuntimeException {
    public ConfigurationException(final String message) {
        super(message);
    }

    public ConfigurationException(final String message, final Exception e) {
        super(message, e);
    }
}
