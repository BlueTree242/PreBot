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

package me.bluetree242.prebot.api;

import me.bluetree242.jdaeventer.JDAEventer;
import me.bluetree242.prebot.api.commands.console.ConsoleCommandManager;
import me.bluetree242.prebot.api.plugin.PluginManager;
import me.bluetree242.prebot.config.PreBotConfig;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * The main class of PreBot
 */
public abstract class PreBot {

    private static PreBot bot;

    /**
     * Only classes that extend PreBot may call this
     */
    protected PreBot() {
    }

    /**
     * gets the current instance of PreBot.
     * This is never null when plugins are being loaded.
     * @return the instance of PreBot currently running
     * @throws RuntimeException if the implementation of PreBot is not set
     */
    @Nullable
    public static PreBot getInstance() {
        return bot;
    }

    /**
     * Sets the prebot instance.
     *
     * @param prebot the instance to set
     */
    public static void setPreBot(@NotNull PreBot prebot) {
        if (bot != null)
            throw new UnsupportedOperationException("Instance is already set, and it is not closed.");
        bot = prebot;
    }


    /**
     * Gives you the current shard manager.
     *
     * @return the shard manager, or null if the bot has not started yet
     */
    @Nullable
    public abstract ShardManager getShardManager();

    /**
     * The plugin manager of PreBot
     *
     * @return the Plugin Manager
     */
    public abstract PluginManager getPluginManager();

    /**
     * Reloads the prebot configuration.
     */
    public abstract void reloadConfig();

    /**
     * Returns the PreBot config
     *
     * @return the prebot config
     */
    @NotNull
    public abstract PreBotConfig getConfig();

    /**
     * The JDAEventer for this instance
     *
     * @return The eventer instance.
     */
    @NotNull
    public abstract JDAEventer getEventer();

    /**
     * get the thread pool used to handle events and more
     *
     * @return the executor PreBot uses. for events and more.
     */
    @NotNull
    public abstract ThreadPoolExecutor getExecutor();

    /**
     * Requires intents to be enabled. Call this and the intent is guaranteed enabled when the bot starts.
     *
     * @param intents intents to require
     */
    public abstract void requireIntents(GatewayIntent... intents);

    /**
     * The intents that has been required. NOTE: This is unmodifiable after initialization of {@link ShardManager}
     *
     * @return the required gateway intents
     */
    public abstract Set<GatewayIntent> getIntents();

    /**
     * Requires cache flags to be enabled. Call this and the cache flag is guaranteed enabled when the bot starts.
     *
     * @param cacheFlags cache flags to enable to require
     */
    public abstract void requireCacheFlags(CacheFlag... cacheFlags);

    /**
     * The cache flags that has been required. NOTE: This is unmodifiable after initialization of {@link ShardManager}
     *
     * @return the required cache flags
     */
    public abstract Set<CacheFlag> getCacheFlags();

    /**
     * get the root directory of prebot, usually at the location of the application.
     *
     * @return the root directory of prebot
     */
    public abstract Path getRootDirectory();

    /**
     * The console command manager.
     * @return the console command manager.
     */
    public abstract ConsoleCommandManager getConsoleCommandManager();

    /**
     * if prebot has stopped, or is being stopped
     * @return true if prebot is stopped or being stopped, false otherwise
     */
    public abstract boolean isStopped();

    /**
     * if prebot has started, and {@link PreBot#getShardManager()} has been initialized, this is always true even if {@link PreBot#isStopped()} is true.
     * @return true if prebot has started, false otherwise
     */
    public abstract boolean isStarted();

    /**
     * Starts to disable all plugins, and shuts down {@link PreBot#getShardManager()}
     */
    public abstract void stop();
}
