package me.bluetree242.prebot.core.plugin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.bluetree242.prebot.api.LoggerProvider;
import me.bluetree242.prebot.core.plugin.loader.JarPluginClassLoader;
import me.bluetree242.prebot.core.plugin.logging.JarPluginLogger;
import me.bluetree242.prebot.api.plugin.Plugin;
import me.bluetree242.prebot.api.plugin.PluginManager;
import net.dv8tion.jda.api.JDA;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

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
    public void onShardOnline(JDA shard) {

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
