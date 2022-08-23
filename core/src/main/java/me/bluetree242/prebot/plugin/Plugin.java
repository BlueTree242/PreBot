package me.bluetree242.prebot.plugin;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;


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
    PluginDescription getDescription();

    /**
     * The logger this plugin uses, this logger automatically prepends the plugin name at start of any log
     *
     * @return the logger for this plugin
     */
    Logger getLogger();

    /**
     * If the plugin is enabled, this is false when plugin is still loading
     *
     * @return true if the plugin is enabled, false otherwise.
     */
    boolean isEnabled();

    /**
     * Sets the enabled status. This method might call {@link PluginManager#enablePlugin(Plugin)} or {@link PluginManager#disablePlugin(Plugin)}
     *
     * @param enabled enable status to set
     */
    void setEnabled(boolean enabled);
}
