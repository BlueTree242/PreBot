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

package me.bluetree242.prebot.core.plugin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.bluetree242.jdaeventer.DiscordListener;
import me.bluetree242.prebot.api.LoggerProvider;
import me.bluetree242.prebot.api.config.ConfigManager;
import me.bluetree242.prebot.api.plugin.Plugin;
import me.bluetree242.prebot.api.plugin.PluginConfig;
import me.bluetree242.prebot.api.plugin.PluginManager;
import me.bluetree242.prebot.core.plugin.loader.JarPluginClassLoader;
import me.bluetree242.prebot.core.plugin.logging.JarPluginLogger;
import me.bluetree242.prebot.core.utils.Utils;
import net.dv8tion.jda.api.JDA;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents a plugin which is a jar
 */
@RequiredArgsConstructor
public class JarPlugin implements Plugin {
    private static final Logger LOGGER = LoggerProvider.getProvider().getLogger(Plugin.class);
    @Getter
    private final JarPluginDescriptionFile description;
    @Getter
    private final JarPluginClassLoader classLoader;
    @Getter
    private final JarPluginLogger logger;
    @Getter
    private final PluginManager pluginManager;
    @Getter
    private final Set<DiscordListener> listeners = new HashSet<>();
    private final Map<String, PluginConfig> configs = new HashMap<>();
    private final Map<String, ConfigManager<? extends PluginConfig>> confManagers = new HashMap<>();
    private boolean enabled = false;

    public JarPlugin() {
        final ClassLoader cl = this.getClass().getClassLoader();
        if (!(cl instanceof JarPluginClassLoader)) {
            throw new IllegalStateException("To initialize JarPlugin with no-param constructor, it must load using JarPluginClassLoaderP");
        }
        JarPluginClassLoader loader = (JarPluginClassLoader) cl;
        description = loader.getDescription();
        classLoader = loader;
        pluginManager = loader.getPluginManager();
        logger = new JarPluginLogger(LOGGER, description);
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onShardReady(JDA shard) {

    }

    @Override
    public void onShardReconnect(JDA shard) {

    }

    @Override
    public void onLoad() {
    }

    @Override
    public final boolean isEnabled() {
        return enabled;
    }

    @Override
    public final void setEnabled(boolean e) {
        if (e == enabled) throw new IllegalArgumentException("This is already the current status.");
        enabled = e;
        if (e) {
            onEnable();
        } else {
            onDisable();
        }
    }

    @Override
    public Path getDataFolder() {
        return Paths.get(getPreBot().getRootDirectory() + Utils.fileseparator() + "plugins" + Utils.fileseparator() + description.getName());
    }

    @Override
    public @Nullable PluginConfig getConfig(String name) {
        if (!configs.containsKey(name))
            throw new IllegalArgumentException(name + ".yml was never reloaded before, or failed to reload");
        return configs.get(name);
    }

    @Override
    public void reloadConfig(String name, Class<? extends PluginConfig> conf) {
        if (configs.containsKey(name) && !conf.isInstance(configs.get(name))) {
            throw new IllegalArgumentException("this config was reloaded before and does not have the same config interface");
        }
        ConfigManager<? extends PluginConfig> configManager = confManagers.containsKey(name) ? confManagers.get(name) : ConfigManager.create(getDataFolder(), name + ".yml", conf);
        confManagers.put(name, configManager);
        configManager.reloadConfig();
        configs.put(name, configManager.getConfigData());
    }

    @Override
    public void reload() {
        confManagers.forEach((k, v) -> {
            v.reloadConfig();
            configs.put(k, v.getConfigData());
        });
    }

    @Override
    public int compareTo(@NotNull Plugin o) {
        return description.compareTo(o.getDescription());
    }
}
