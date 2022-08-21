package me.bluetree242.prebot.core.plugin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.bluetree242.prebot.core.PreBotMain;
import me.bluetree242.prebot.core.plugin.loader.JarPluginClassLoader;
import me.bluetree242.prebot.core.utils.Utils;
import me.bluetree242.prebot.exceptions.InvalidPluginException;
import me.bluetree242.prebot.plugin.Plugin;
import me.bluetree242.prebot.plugin.PluginManager;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@RequiredArgsConstructor
public class MainPluginManager implements PluginManager {
    private final PreBotMain core;
    @Getter
    private final Set<Plugin> plugins = new HashSet<>();

    public void loadPlugin(File file) throws IOException {
        if (!file.getName().endsWith(".jar")) throw new IllegalArgumentException("File is not a jar file");
        //get details from prebot.yml
        JarFile jar = new JarFile(file);
        JarEntry entry = jar.getJarEntry("prebot.yml");
        if (entry == null) {
            throw new InvalidPluginException("Jar does not contain prebot.yml");
        }
        InputStream stream = jar.getInputStream(entry);
        Yaml yaml = new Yaml();
        Map<String, Object> prebotFile = yaml.load(stream);
        stream.close();
        JarPluginDescriptionFile descriptionFile = new JarPluginDescriptionFile(prebotFile);
        //load the plugin
        JarPluginClassLoader loader = new JarPluginClassLoader(file);
        //now get the main class out of it
        Class<JarPlugin> clazz;
        try {
            Class<?> clz = loader.findClass(descriptionFile.getMain());
            if (!clz.getSuperclass().equals(JarPlugin.class))
                throw new InvalidPluginException("Plugin does not extend JarPlugin");
            clazz = (Class<JarPlugin>) clz;
        } catch (ClassNotFoundException e) {
            throw new InvalidPluginException("Main class " + descriptionFile.getMain() + " was not found");
        }
        JarPlugin plugin;
        try {
            plugin = clazz.getConstructor(JarPluginDescriptionFile.class, JarPluginClassLoader.class).newInstance(descriptionFile, loader);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        plugins.add(plugin);
    }

    public void loadPlugins() {
        File directory = new File(core.getRootDirectory() + Utils.fileseparator() + "plugins");
        directory.mkdirs();
        File[] files = directory.listFiles(file1 -> file1.getName().endsWith(".jar") && !file1.isDirectory());
        for (File file : files) {
            try {
                loadPlugin(file);
            } catch (Exception e) {
                //TODO: log plugin name
                e.printStackTrace();
            }
        }
    }
}
