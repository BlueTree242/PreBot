package me.bluetree242.prebot.api.exceptions.plugin;

/**
 * Represents a plugin that failed to load due to invalid configuration or similar.
 */
public class InvalidPluginException extends RuntimeException {

    public InvalidPluginException(String s) {
        super(s);
    }
}
