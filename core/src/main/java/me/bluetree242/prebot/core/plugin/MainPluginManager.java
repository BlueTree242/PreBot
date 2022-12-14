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
import me.bluetree242.prebot.api.LoggerProvider;
import me.bluetree242.prebot.api.exceptions.plugin.InvalidPluginException;
import me.bluetree242.prebot.api.exceptions.plugin.MissingDependenciesException;
import me.bluetree242.prebot.api.plugin.Plugin;
import me.bluetree242.prebot.api.plugin.PluginManager;
import me.bluetree242.prebot.core.PreBotMain;
import me.bluetree242.prebot.core.plugin.loader.JarPluginClassLoader;
import me.bluetree242.prebot.core.utils.Utils;
import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MainPluginManager implements PluginManager {
    private static final Logger LOGGER = LoggerProvider.getProvider().getLogger(MainPluginManager.class);
    private final PreBotMain core;
    @Getter
    private final Set<Plugin> plugins = new HashSet<>();

    @Override
    public void loadPlugin(File file) throws IOException, InvalidPluginException {
        if (!file.getName().endsWith(".jar")) throw new IllegalArgumentException("File is not a jar file");
        //load the plugin
        loadPlugin(loadDescription(file), file);
    }

    @Override
    public void enablePlugin(Plugin plugin) {
        LOGGER.info("Enabling plugin {} v{}", plugin.getDescription().getName(), plugin.getDescription().getVersion());
        plugin.setEnabled(true);
        plugin.getListeners().forEach(l -> core.getEventer().addListener(l));
    }

    @Override
    public void disablePlugin(Plugin plugin) {
        LOGGER.info("Disabling plugin {} v{}", plugin.getDescription().getName(), plugin.getDescription().getVersion());
        plugin.getListeners().forEach(l -> core.getEventer().removeListener(l));
        plugin.getListeners().clear(); //clear all listeners
        plugin.setEnabled(false);
    }

    @Override
    public Plugin getPluginByName(String name) {
        for (Plugin plugin : plugins) {
            if (plugin.getDescription().getName().equals(name)) return plugin;
        }
        return null;
    }

    public JarPluginDescriptionFile loadDescription(File file) throws IOException, MissingDependenciesException {
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
        return new JarPluginDescriptionFile(prebotFile, file);
    }

    @SuppressWarnings({"unchecked", "resource"})
    public void loadPlugin(JarPluginDescriptionFile descriptionFile, File file) throws MalformedURLException {
        LOGGER.info("Loading plugin {} v{}", descriptionFile.getName(), descriptionFile.getVersion());
        Set<String> missingDependencies = new HashSet<>();
        for (String dependency : descriptionFile.getDependencies()) {
            boolean found = false;
            for (Plugin plugin : plugins) {
                if (plugin.getDescription().getName().equals(dependency)) found = true;
            }
            if (!found) missingDependencies.add(dependency);
        }
        if (!missingDependencies.isEmpty()) {
            throw new MissingDependenciesException(descriptionFile, missingDependencies);
        }
        JarPluginClassLoader loader = new JarPluginClassLoader(file, descriptionFile, this);
        //now get the main class out of it
        Class<JarPlugin> clazz;
        try {
            Class<?> clz = loader.findClass(descriptionFile.getMain());
            if (!clz.getSuperclass().equals(JarPlugin.class))
                throw new InvalidPluginException(clz.getName() + " Does not extend JarPlugin");
            clazz = (Class<JarPlugin>) clz;
        } catch (ClassNotFoundException e) {
            throw new InvalidPluginException("Main class " + descriptionFile.getMain() + " was not found");
        }
        JarPlugin plugin;
        try {
            plugin = clazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        plugin.onLoad();
        core.requireIntents(descriptionFile.getRequiredIntents()); //require the intents required by the plugin
        core.requireCacheFlags(descriptionFile.getRequiredCacheFlags());
        plugins.add(plugin);
    }

    public void loadPlugins() {
        File directory = new File(core.getRootDirectory() + Utils.fileseparator() + "plugins");
        if (!directory.mkdirs() && !directory.exists()) {
            throw new IllegalStateException("Failed to create plugins folder");
        }
        File[] files = directory.listFiles(file1 -> file1.getName().endsWith(".jar") && !file1.isDirectory());
        if (files == null) throw new RuntimeException();
        Map<String, JarPluginDescriptionFile> descriptions = new HashMap<>();
        for (File file : files) {
            try {
                JarPluginDescriptionFile description = loadDescription(file);
                if (descriptions.containsKey(description.getName())) {
                    LOGGER.error("{} has the same plugin name of {}", description.getJarFile().getName(), descriptions.get(description.getName()).getJarFile().getName());
                    continue;
                }
                descriptions.put(description.getName(), description);
            } catch (Exception e) {
                LOGGER.error("Failed to initialize plugin " + file.getName(), e);
            }
        }
        List<JarPluginDescriptionFile> descriptionsSorted = descriptions.values().stream().sorted().collect(Collectors.toList());
        for (JarPluginDescriptionFile descriptionFile : descriptionsSorted) {
            try {
                loadPlugin(descriptionFile, descriptionFile.getJarFile());
            } catch (MissingDependenciesException e) {
                e.log(); //log it instead of printing stack track
            } catch (UnsupportedClassVersionError e) {
                LOGGER.error("Failed to load plugin {} v{} because it requires a more modern version of java, please upgrade your java version and try again", descriptionFile.getName(), descriptionFile.getVersion());
                if (LOGGER.isDebugEnabled()) e.printStackTrace();
            } catch (Throwable e) {
                LOGGER.error("An error occurred while loading " + descriptionFile.getName() + " v" + descriptionFile.getVersion(), e);
            }
        }
        //now enable the plugins
        for (Plugin plugin : plugins.stream().sorted().collect(Collectors.toList())) {
            try {
                enablePlugin(plugin);
            } catch (Exception ex) {
                LOGGER.error("An error occurred while enabling " + plugin.getDescription().getName() + " v" + plugin.getDescription().getVersion(), ex);
                try {
                    disablePlugin(plugin);
                } catch (Exception ex2) {
                    LOGGER.error("An error occurred while disabling " + plugin.getDescription().getName() + " v" + plugin.getDescription().getVersion(), ex);
                }
            }
        }
    }

    public void disablePlugins() {
        List<Plugin> pluginsSorted = plugins.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        for (Plugin plugin : pluginsSorted) {
            if (plugin.isEnabled()) disablePlugin(plugin);
        }
    }
}
