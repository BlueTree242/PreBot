package me.bluetree242.prebot.plugin;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface PluginDescription {

    /**
     * The Version of the plugin
     *
     * @return the version of the plugin
     */
    @NotNull
    String getVersion();

    /**
     * The authors of the plugin
     *
     * @return Unmodifiable list of authors, cannot be less than 1
     */
    @NotNull
    List<String> getAuthors();

    /**
     * The name of the plugin
     *
     * @return The name of the plugin.
     */
    @NotNull
    String getName();

    /**
     * The path to the main class of the plugin
     *
     * @return the main class path
     */
    @NotNull
    String getMain();

    /**
     * the dependencies of the plugin
     *
     * @return The list of dependencies of the plugin, never null
     */
    @NotNull
    List<String> getDependencies();

    /**
     * the soft dependencies of the plugin, soft dependencies are optional dependencies
     *
     * @return The list of soft dependencies of the plugin, never null
     */
    @NotNull
    List<String> getSoftDependencies();
}
