package me.bluetree242.prebot.exceptions;

import lombok.Getter;
import me.bluetree242.prebot.LoggerFactory;
import me.bluetree242.prebot.plugin.PluginDescription;
import org.slf4j.Logger;

import java.util.Set;

public class MissingDependenciesException extends InvalidPluginException{
    private final Logger LOGGER = LoggerFactory.getFactory().getLogger(MissingDependenciesException.class);
    @Getter
    private final PluginDescription description;
    private final Set<String> missingDependencies;
    public MissingDependenciesException(PluginDescription description, Set<String> missingDependencies) {
        super(description.getName() + " requires " + missingDependencies.size() + " dependency(s). Please install (" + String.join(", ", missingDependencies) + ")");
        this.description = description;
        this.missingDependencies = missingDependencies;
    }

    public void log() {
        LOGGER.error(getMessage());
    }
}
