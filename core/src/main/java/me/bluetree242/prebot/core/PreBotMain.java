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
import me.bluetree242.jdaeventer.JDAEventer;
import me.bluetree242.prebot.api.LoggerProvider;
import me.bluetree242.prebot.api.PreBot;
import me.bluetree242.prebot.api.config.ConfigManager;
import me.bluetree242.prebot.core.config.PreBotConfig;
import me.bluetree242.prebot.core.listener.PreBotListener;
import me.bluetree242.prebot.core.plugin.MainPluginManager;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.slf4j.Logger;

import javax.security.auth.login.LoginException;
import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class PreBotMain extends PreBot {
    private static final Logger LOGGER = LoggerProvider.getProvider().getLogger(PreBotMain.class);
    @Getter
    private final Path rootDirectory;
    @Getter
    private final MainPluginManager pluginManager = new MainPluginManager(this);
    @Getter
    private ShardManager shardManager;
    @Getter
    private PreBotConfig config;
    @Getter
    private ConfigManager<PreBotConfig> configManager;
    @Getter
    private final JDAEventer eventer = new JDAEventer();
    @Getter
    private ThreadPoolExecutor executor;
    public PreBotMain(Path rootDirectory) {
        this.rootDirectory = rootDirectory;
        start();
    }

    private void start() {
        LOGGER.info("Starting PreBot.. ");
        LOGGER.info("Loading configuration..");
        reloadConfig();
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(config.executor_size());
        LOGGER.info("Loading Plugins..");
        pluginManager.loadPlugins();
        addListeners();
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(config.token());
        builder.setEnableShutdownHook(false)
                .setStatus(config.online_status())
                .setActivity(getActivity())
                .setEventPool(executor)
                .addEventListeners(eventer.getRootListener());
        try {
            shardManager = builder.build(true);
        } catch (LoginException e) {
            LOGGER.error("The provided token in config.yml is invalid. Please make sure the token is correct and try again.");
        }
    }

    private void addListeners() {
        eventer.addListener(new PreBotListener(this));
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
        if (configManager == null) configManager = ConfigManager.create(rootDirectory, "config.yml", PreBotConfig.class);
        configManager.reloadConfig();
        config = configManager.getConfigData();
    }
}
