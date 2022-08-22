package me.bluetree242.prebot.core;

import lombok.Getter;
import me.bluetree242.prebot.LoggerProvider;
import me.bluetree242.prebot.PreBot;
import me.bluetree242.prebot.core.plugin.MainPluginManager;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.slf4j.Logger;

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
    private boolean closed = false;

    public PreBotMain(Path rootDirectory) {
        this.rootDirectory = rootDirectory;
        start();
    }

    private void start() {
        LOGGER.info("Starting PreBot.. ");
        pluginManager.loadPlugins();
    }
}
