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

package me.bluetree242.prebot.core;

import lombok.Getter;
import lombok.SneakyThrows;
import me.bluetree242.jdaeventer.JDAEventer;
import me.bluetree242.prebot.api.LoggerProvider;
import me.bluetree242.prebot.api.PreBot;
import me.bluetree242.prebot.api.PreBotVersion;
import me.bluetree242.prebot.api.commands.discord.DiscordCommandManager;
import me.bluetree242.prebot.api.config.ConfigManager;
import me.bluetree242.prebot.api.events.ShardManagerPreBuildEvent;
import me.bluetree242.prebot.config.PreBotConfig;
import me.bluetree242.prebot.core.command.console.MainConsoleCommandManager;
import me.bluetree242.prebot.core.command.discord.MainDiscordCommandManager;
import me.bluetree242.prebot.core.consolecommands.*;
import me.bluetree242.prebot.core.discordcommands.PreBotDiscordCommand;
import me.bluetree242.prebot.core.listener.PreBotListener;
import me.bluetree242.prebot.core.plugin.MainPluginManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import javax.security.auth.login.LoginException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

public class PreBotMain extends PreBot {
    private static final Logger LOGGER = LoggerProvider.getProvider().getLogger(PreBotMain.class);
    @Getter
    private final Path rootDirectory;
    @Getter
    private final MainPluginManager pluginManager = new MainPluginManager(this);
    @Getter
    private final JDAEventer eventer = new JDAEventer();
    @Getter
    private final MainConsoleCommandManager consoleCommandManager = new MainConsoleCommandManager(this);
    @Getter
    private final DiscordCommandManager discordCommandManager = new MainDiscordCommandManager(this);
    @Getter
    private ShardManager shardManager;
    @Getter
    private PreBotConfig config;
    @Getter
    private ConfigManager<PreBotConfig> configManager;
    @Getter
    private ThreadPoolExecutor executor;
    @Getter
    private Set<GatewayIntent> intents = new HashSet<>();
    @Getter
    private Set<CacheFlag> cacheFlags = new HashSet<>();
    @Getter
    private boolean stopped = false;
    @Getter
    private boolean started;
    @Getter
    private long startTime = System.currentTimeMillis();
    public PreBotMain(Path rootDirectory) {
        PreBot.setPreBot(this);
        this.rootDirectory = rootDirectory;
        start();
    }

    private void start() {
        LOGGER.info("Starting PreBot {}...", PreBotVersion.VERSION);
        LOGGER.info("Loading configuration..");
        reloadConfig();
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(config.executor_size(), new ThreadFactory() {
            int num = 0;

            @Override
            public Thread newThread(@NotNull Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("PreBot-Executor-" + (num = num + 1));
                thread.setDaemon(true);
                return thread;
            }
        });
        addDiscordCommands();
        addConsoleCommands();
        LOGGER.info("Loading Plugins..");
        pluginManager.loadPlugins();
        addListeners();
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(config.token());
        builder.setEnableShutdownHook(false)
                .setStatusProvider(s -> config.online_status())
                .setActivityProvider(s -> getActivity())
                .setEventPool(executor, false)
                .enableIntents(intents)
                .setMemberCachePolicy(MemberCachePolicy.VOICE.or(MemberCachePolicy.OWNER))
                .enableCache(cacheFlags)
                .addEventListeners(eventer.getRootListener());
        eventer.fireEvent(new ShardManagerPreBuildEvent(builder));
        intents = Collections.unmodifiableSet(intents); //now it is unmodifiable
        cacheFlags = Collections.unmodifiableSet(cacheFlags);
        startTime = System.currentTimeMillis();
        try {
            shardManager = builder.build(true);
            started = true;
        } catch (LoginException e) {
            LOGGER.error("The provided token in config.yml is invalid. Please make sure the token is correct and try again.");
        }
    }

    private void addListeners() {
        eventer.addListener(new PreBotListener(this));
    }

    private void addDiscordCommands() {
        getDiscordCommandManager().registerCommands(new PreBotDiscordCommand(this));
    }

    private void addConsoleCommands() {
        consoleCommandManager.registerCommands(new VersionConsoleCommand(this),
                new StopConsoleCommand(this),
                new HelpConsoleCommand(this),
                new PluginsConsoleCommand(this),
                new ReloadConsoleCommand(this));
    }

    public Activity getActivity() {
        String activity = config.activity_status();
        if (activity.startsWith("playing ")) {
            return Activity.playing(activity.replaceFirst("playing ", ""));
        } else if (activity.startsWith("watching ")) {
            return Activity.watching(activity.replaceFirst("watching ", ""));
        } else if (activity.startsWith("streaming ")) {
            return Activity.streaming(activity.replaceFirst("streaming ", ""), null);
        } else if (activity.startsWith("competing in ")) {
            return Activity.competing(activity.replaceFirst("competing in ", ""));
        } else return Activity.playing(activity);
    }

    @Override
    public void reloadConfig() {
        if (configManager == null)
            configManager = ConfigManager.create(rootDirectory, "config.yml", PreBotConfig.class);
        configManager.reloadConfig();
        config = configManager.getConfigData();
    }

    @Override
    public void requireIntents(GatewayIntent... intents) {
        Collections.addAll(this.intents, intents);
    }

    @Override
    public void requireCacheFlags(CacheFlag... cacheFlags) {
        Collections.addAll(this.cacheFlags, cacheFlags);
    }

    @SneakyThrows
    @Override
    public void stop() {
        if (stopped) return; //already stopped/stopping
        LOGGER.info("Shutting down PreBot..");
        stopped = true;
        //make sure no shards are reconnecting
        while (shardManager.getShards().stream().anyMatch(j -> j.getStatus() != JDA.Status.CONNECTED && j.getStatus() != JDA.Status.DISCONNECTED))
            Thread.sleep(100);
        pluginManager.disablePlugins();
        LOGGER.info("Shutting down Shard Manager..");
        shardManager.shutdown();
    }

    @Override
    public boolean isAdmin(Guild guild) {
        return config.admin_guilds().contains(guild.getIdLong());
    }

    @Override
    public boolean isAdmin(UserSnowflake user) {
        return config.admin_users().contains(user.getIdLong());
    }

    @Override
    public void reload() {
        reloadConfig();
        getDiscordCommandManager().registerCommands(shardManager.getGuilds());
    }
}
