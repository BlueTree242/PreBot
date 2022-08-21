package me.bluetree242.prebot.plugin;

import org.jetbrains.annotations.NotNull;


/**
 * Represents a PreBot Plugin
 */
public interface Plugin {

    /**
     * if the plugin is visible in the plugin list or not
     *
     * @return true if the plugin is visible in the plugin list
     */
    boolean isVisible();

    /**
     * Called when the plugin is being enabled
     */
    void onEnable();

    /**
     * Called after the plugin is being disabled
     */
    void onDisable();

    /**
     * Called when the discord bot goes online
     */
    void onBotOnline();

    /**
     * Called directly after loading the plugin (all installed plugins should be loaded before that is called)
     */
    void onLoad();

    /**
     * Returns the classloader of this plugin
     *
     * @return The class loader of the plugin
     */
    ClassLoader getClassLoader();

    /**
     * the description this plugin belongs to
     *
     * @return the plugin description
     */
    @NotNull
    PluginDescriptionFile getDescription();
}
