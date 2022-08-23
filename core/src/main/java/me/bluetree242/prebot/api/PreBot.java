package me.bluetree242.prebot.api;

import me.bluetree242.prebot.api.plugin.PluginManager;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PreBot {

    private static PreBot bot;

    /**
     * gets the current instance of PreBot
     *
     * @return the instance of PreBot currently running and not closed
     * @throws RuntimeException if the implementation of PreBot is not set
     */
    @Nullable
    public static PreBot getInstance() {
        if (bot == null || bot.isClosed()) throw new RuntimeException("Bot Implementation was not set");
        return bot;
    }

    /**
     * Sets the prebot instance, this should only be used by the core
     *
     * @param prebot the instance to set
     */
    public void setPreBot(@NotNull PreBot prebot) {
        if (bot != null && !bot.isClosed())
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
     * if the instance is closed, if this is closed it means it is useless
     * @return true if this instance is closed
     */
    public abstract boolean isClosed();

    /**
     * The plugin manager of PreBot
     *
     * @return the Plugin Manager
     */
    public abstract PluginManager getPluginManager();
}
