package me.bluetree242.prebot.core.plugin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.bluetree242.prebot.core.LoggerFactory;
import me.bluetree242.prebot.core.plugin.loader.JarPluginClassLoader;
import me.bluetree242.prebot.core.plugin.logging.JarPluginLogger;
import me.bluetree242.prebot.plugin.Plugin;
import me.bluetree242.prebot.plugin.PluginManager;
import org.slf4j.Logger;

@RequiredArgsConstructor
public class JarPlugin implements Plugin {
    @Getter
    private final JarPluginDescriptionFile description;
    @Getter
    private final JarPluginClassLoader classLoader;
    @Getter
    private final JarPluginLogger logger;
    @Getter
    private final PluginManager pluginManager;
    private boolean enabled = false;

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
