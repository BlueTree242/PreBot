package me.bluetree242.prebot.core.plugin.loader;

import lombok.Getter;
import me.bluetree242.prebot.core.PreBotMain;
import me.bluetree242.prebot.core.plugin.JarPluginDescriptionFile;
import me.bluetree242.prebot.api.plugin.PluginManager;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class JarPluginClassLoader extends URLClassLoader {
    @Getter
    private final JarPluginDescriptionFile description;
    @Getter
    private final PluginManager pluginManager;
    public JarPluginClassLoader(File file, JarPluginDescriptionFile description, PluginManager pluginManager) throws MalformedURLException {
        super(new URL[]{file.toURI().toURL()}, PreBotMain.class.getClassLoader());
        this.description = description;
        this.pluginManager = pluginManager;
    }

    public Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }
}
