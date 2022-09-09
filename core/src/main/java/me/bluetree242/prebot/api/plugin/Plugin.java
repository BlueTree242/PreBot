/*
 * LICENSE
 * PreBot
 * -------------
 * Copyright (C) 2022 - 2022 BlueTree242
 * -------------
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * END
 */

package me.bluetree242.prebot.api.plugin;

import me.bluetree242.jdaeventer.DiscordListener;
import me.bluetree242.prebot.api.PreBot;
import me.bluetree242.prebot.api.plugin.commands.console.PluginConsoleCommand;
import me.bluetree242.prebot.api.plugin.commands.discord.PluginDiscordCommand;
import net.dv8tion.jda.api.JDA;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Represents a PreBot Plugin
 */
public interface Plugin extends Comparable<Plugin> {

    /**
     * Called when the plugin is being enabled
     */
    void onEnable();

    /**
     * Called after the plugin is being disabled
     */
    void onDisable();

    /**
     * Called when a shard is ready.
     * This is only called when plugin is enabled
     *
     * @param shard the shard that has just went ready
     * @see net.dv8tion.jda.api.events.ReadyEvent
     */
    void onShardReady(JDA shard);

    /**
     * Called when a shard has reconnected.
     * This is only called when plugin is enabled
     *
     * @param shard the shard that has just reconnected
     * @see net.dv8tion.jda.api.events.ReconnectedEvent
     */
    void onShardReconnect(JDA shard);

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
     *
     * @return the current prebot instance
     * @see PreBot#getInstance()
     */
    @NotNull
    default PreBot getPreBot() {
        return Objects.requireNonNull(PreBot.getInstance());
    }

    /**
     * Get the listeners registered by the plugin, this list is cleared after the plugin is disabled
     *
     * @return the listeners registered by this plugin
     */
    @NotNull
    Set<DiscordListener> getListeners();

    /**
     * Registers a listener, by default this adds a listener to {@link Plugin#getListeners()}, which is cleared after the plugin is disabled.
     *
     * @param listeners listeners to register.
     */
    default void registerListeners(DiscordListener... listeners) {
        Collections.addAll(getListeners(), listeners);
    }

    /**
     * Removes a listener, by default this removes listeners from {@link Plugin#getListeners()}, which is cleared after the plugin is disabled.
     *
     * @param listeners listeners to remove.
     */
    default void removeListeners(DiscordListener... listeners) {
        Set<DiscordListener> listenerSet = Arrays.stream(listeners).collect(Collectors.toSet());
        getListeners().removeAll(listenerSet);
    }

    /**
     * get the data folder where this plugin stores it's data
     *
     * @return the data folder of the plugin
     */
    Path getDataFolder();

    /**
     * gets a config by its name.
     *
     * @param name name of the config without extension
     * @return the configuration by name
     * @throws IllegalArgumentException if the config by this name was never reloaded.
     * @see Plugin#reloadConfig(String, Class)
     */
    PluginConfig getConfig(String name) throws IllegalArgumentException;

    /**
     * reloads (or loads) a configuration by name
     *
     * @param name name of the configuration, without extension
     * @param conf config interface
     * @see Plugin#getConfig(String)
     */
    void reloadConfig(String name, Class<? extends PluginConfig> conf);

    /**
     * gets the main config, the config.yml
     *
     * @return your main configuration
     * @throws IllegalArgumentException if the main config was never reloaded.
     * @see Plugin#reloadConfig(Class)
     */
    default PluginConfig getConfig() throws IllegalArgumentException {
        return getConfig("config");
    }

    /**
     * reloads (or loads) the main configuration (config.yml)
     *
     * @param conf config interface
     * @see Plugin#getConfig()
     */
    default void reloadConfig(Class<? extends PluginConfig> conf) {
        reloadConfig("config", conf);
    }

    /**
     * Registers console commands to PreBot, controlled by this plugin
     *
     * @param commands commands to register
     */
    default void registerConsoleCommands(PluginConsoleCommand... commands) {
        getPreBot().getConsoleCommandManager().registerCommands(commands);
    }

    /**
     * Registers some commands
     *
     * @param commands commands to register
     */
    default void registerCommands(PluginDiscordCommand... commands) {
        getPreBot().getDiscordCommandManager().registerCommands(commands);
    }
}
