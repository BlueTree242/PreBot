package me.bluetree242.prebot.plugin;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface PluginDescriptionFile {

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
}
