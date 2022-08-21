package me.bluetree242.prebot.core.plugin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.bluetree242.prebot.core.plugin.loader.JarPluginClassLoader;
import me.bluetree242.prebot.plugin.Plugin;

@RequiredArgsConstructor
public class JarPlugin implements Plugin {
    @Getter
    private final JarPluginDescriptionFile description;
    @Getter
    private final JarPluginClassLoader classLoader;

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
}
