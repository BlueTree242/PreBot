package me.bluetree242.prebot.exceptions;

/**
 * Represents a plugin that failed to load due to invalid configuration or simillar.
 */
public class InvalidPluginException extends RuntimeException {

    public InvalidPluginException(String s) {
        super(s);
    }
}
