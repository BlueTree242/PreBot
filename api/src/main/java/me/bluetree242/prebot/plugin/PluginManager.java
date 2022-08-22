package me.bluetree242.prebot.plugin;

import me.bluetree242.prebot.PreBot;
import me.bluetree242.prebot.exceptions.InvalidPluginException;
import me.bluetree242.prebot.exceptions.MissingDependenciesException;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * This interface is the plugin manager which manage plugins (core implements it)
 * @see PreBot#getPluginManager()
 */
public interface PluginManager {

    /**
     * Loads a plugin jar file
     *
     * @param file the jar file to load
     * @throws IOException            if an IO exception occurs
     * @throws InvalidPluginException if the plugin is not valid
     * @throws MissingDependenciesException if the plugin has dependencies that are missing
     */
    void loadPlugin(File file) throws IOException, InvalidPluginException, MissingDependenciesException;

    /**
     * The set of plugins that exist, can be disabled
     *
     * @return the unmodifiable set of plugins
     */
    Set<Plugin> getPlugins();

    /**
     * Enables a plugin, this might call {@link Plugin#onEnable()} and {@link  Plugin#onDisable()}
     *
     * @param plugin Plugin to enable
     */
    void enablePlugin(Plugin plugin);

    /**
     * Disables a plugin, this might call @link  Plugin#onDisable()}
     *
     * @param plugin Plugin to disable
     */
    void disablePlugin(Plugin plugin);
}
