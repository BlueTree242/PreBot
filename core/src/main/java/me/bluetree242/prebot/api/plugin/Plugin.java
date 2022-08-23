package me.bluetree242.prebot.api.plugin;

import me.bluetree242.prebot.api.PreBot;
import net.dv8tion.jda.api.JDA;
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
     * Called when a shard gets online
     * @param shard the shard that has just went online
     */
    void onShardOnline(JDA shard);

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
     * Sets the enabled status. This method might call {@link me.bluetree242.prebot.api.plugin.PluginManager#enablePlugin(Plugin)} or {@link PluginManager#disablePlugin(Plugin)}
     *
     * @param enabled enable status to set
     */
    void setEnabled(boolean enabled);

    /**
     * Provides the PreBot instance
     * @return the current prebot instance
     * @see PreBot#getInstance()
     */
    default PreBot getPreBot() {
        return PreBot.getInstance();
    }
}
