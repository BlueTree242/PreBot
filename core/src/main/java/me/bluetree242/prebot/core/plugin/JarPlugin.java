package me.bluetree242.prebot.core.plugin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.bluetree242.prebot.api.LoggerProvider;
import me.bluetree242.prebot.core.plugin.loader.JarPluginClassLoader;
import me.bluetree242.prebot.core.plugin.logging.JarPluginLogger;
import me.bluetree242.prebot.api.plugin.Plugin;
import me.bluetree242.prebot.api.plugin.PluginDescription;
import me.bluetree242.prebot.api.plugin.PluginManager;
import org.slf4j.Logger;

/**
 * Represents a plugin which is a jar
 */
@RequiredArgsConstructor
public class JarPlugin implements Plugin {
    private static final Logger LOGGER = LoggerProvider.getProvider().getLogger(Plugin.class);
    @Getter
    private final PluginDescription description;
    @Getter
    private final ClassLoader classLoader;
    @Getter
    private final Logger logger;
    @Getter
    private final PluginManager pluginManager;
    private boolean enabled = false;

    public JarPlugin() {
        final ClassLoader cl = this.getClass().getClassLoader();
        if (!(cl instanceof JarPluginClassLoader)) {
            throw new IllegalStateException("JavaPlugin requires " + JarPluginClassLoader.class.getName() + " class loader");
        }
        JarPluginClassLoader loader = (JarPluginClassLoader) cl;
        description = loader.getDescription();
        classLoader = cl;
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
    public void onBotOnline() {
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
}
