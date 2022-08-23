package me.bluetree242.prebot.api.config;

import me.bluetree242.prebot.api.exceptions.config.InvalidSyntaxException;
import me.bluetree242.prebot.api.exceptions.config.InvalidTypeException;
import space.arim.dazzleconf.ConfigurationFactory;
import space.arim.dazzleconf.ConfigurationOptions;
import space.arim.dazzleconf.error.ConfigFormatSyntaxException;
import space.arim.dazzleconf.error.InvalidConfigException;
import space.arim.dazzleconf.ext.snakeyaml.CommentMode;
import space.arim.dazzleconf.ext.snakeyaml.SnakeYamlConfigurationFactory;
import space.arim.dazzleconf.ext.snakeyaml.SnakeYamlOptions;
import space.arim.dazzleconf.helper.ConfigurationHelper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

public final class ConfigManager<C> {

    private final ConfigurationHelper<C> configHelper;
    private volatile C configData;
    private final String fileName;

    private ConfigManager(ConfigurationHelper<C> configHelper, String fileName) {
        this.configHelper = configHelper;
        this.fileName = fileName;
    }

    public static <C> ConfigManager<C> create(Path configFolder, String fileName, Class<C> configClass) {
        // SnakeYaml example
        SnakeYamlOptions yamlOptions = new SnakeYamlOptions.Builder()
                .commentMode(CommentMode.alternativeWriter()) // Enables writing YAML comments
                .build();
        ConfigurationFactory<C> configFactory = SnakeYamlConfigurationFactory.create(
                configClass,
                ConfigurationOptions.defaults(), // change this if desired
                yamlOptions);
        return new ConfigManager<>(new ConfigurationHelper<>(configFolder, fileName, configFactory), fileName);
    }

    public void reloadConfig() {
        try {
            configData = configHelper.reloadConfigData();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);

        } catch (ConfigFormatSyntaxException ex) {
            throw new InvalidSyntaxException(fileName);
        } catch (InvalidConfigException ex) {
            throw new InvalidTypeException(fileName);
        }
    }

    public C getConfigData() {
        C configData = this.configData;
        if (configData == null) {
            throw new IllegalStateException("Configuration has not been loaded yet");
        }
        return configData;
    }

}
