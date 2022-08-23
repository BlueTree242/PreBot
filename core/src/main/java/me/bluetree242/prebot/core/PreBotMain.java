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
    public PreBotMain(Path rootDirectory) {
        this.rootDirectory = rootDirectory;
        start();
    }

    private void start() {
        LOGGER.info("Starting PreBot.. ");
        LOGGER.info("Loading configuration..");
        reloadConfig();
        LOGGER.info("Loading Plugins..");
        pluginManager.loadPlugins();
        addListeners();
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(config.token());
        builder.setEnableShutdownHook(false)
                .setStatus(config.online_status())
                .setActivity(getActivity())
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
