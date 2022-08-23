package me.bluetree242.prebot.exceptions;

import lombok.Getter;
import me.bluetree242.prebot.LoggerProvider;
import me.bluetree242.prebot.plugin.PluginDescription;
import org.slf4j.Logger;

import java.util.Set;

/**
 * Thrown when trying to load a plugin and it has missing dependencies
 */
public class MissingDependenciesException extends InvalidPluginException{
    private final Logger LOGGER = LoggerProvider.getProvider().getLogger(MissingDependenciesException.class);
    public MissingDependenciesException(PluginDescription description, Set<String> missingDependencies) {
        super(description.getName() + " requires " + missingDependencies.size() + " dependency(s). Please install (" + String.join(", ", missingDependencies) + ")");
    }

    public void log() {
        LOGGER.error(getMessage());
    }
}
