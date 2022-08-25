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
import me.bluetree242.prebot.core.plugin.loader.JarPluginClassLoader;
import me.bluetree242.prebot.core.plugin.logging.JarPluginLogger;
import me.bluetree242.prebot.api.plugin.Plugin;
import me.bluetree242.prebot.api.plugin.PluginManager;
import net.dv8tion.jda.api.JDA;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a plugin which is a jar
 */
@RequiredArgsConstructor
public class JarPlugin implements Plugin, Comparable<JarPlugin> {
    private static final Logger LOGGER = LoggerProvider.getProvider().getLogger(Plugin.class);
    @Getter
    private final JarPluginDescriptionFile description;
    @Getter
    private final JarPluginClassLoader classLoader;
    @Getter
    private final JarPluginLogger logger;
    @Getter
    private final PluginManager pluginManager;
    private boolean enabled = false;
    @Getter
    private final Set<DiscordListener> listeners = new HashSet<>();
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
    public final boolean isVisible() {
        return true;
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
    public int compareTo(@NotNull JarPlugin o) {
        return description.compareTo(o.description);
    }
}
