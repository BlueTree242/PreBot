package me.bluetree242.prebot.api;

import me.bluetree242.prebot.api.plugin.PluginManager;
import me.bluetree242.prebot.core.config.PreBotConfig;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PreBot {

    private static PreBot bot;

    /**
     * gets the current instance of PreBot
     *
     * @return the instance of PreBot currently running
     * @throws RuntimeException if the implementation of PreBot is not set
     */
    @Nullable
    public static PreBot getInstance() {
        if (bot == null) throw new RuntimeException("Bot Implementation was not set");
        return bot;
    }

    /**
     * Sets the prebot instance.
     *
     * @param prebot the instance to set
     */
    public void setPreBot(@NotNull PreBot prebot) {
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
     * @return the prebot config
     */
    @NotNull
    public abstract PreBotConfig getConfig();
}
