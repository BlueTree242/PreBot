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

package me.bluetree242.prebot.core.plugin.loader;

import lombok.Getter;
import me.bluetree242.prebot.api.LoggerProvider;
import me.bluetree242.prebot.api.plugin.Plugin;
import me.bluetree242.prebot.api.plugin.PluginManager;
import me.bluetree242.prebot.core.PreBotMain;
import me.bluetree242.prebot.core.plugin.JarPluginDescriptionFile;
import org.slf4j.Logger;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JarPluginClassLoader extends URLClassLoader {
    private static final Logger LOGGER = LoggerProvider.getProvider().getLogger(JarPluginClassLoader.class);
    @Getter
    private final JarPluginDescriptionFile description;
    @Getter
    private final PluginManager pluginManager;
    private final List<String> loadedNonDepend = new ArrayList<>();

    public JarPluginClassLoader(File file, JarPluginDescriptionFile description, PluginManager pluginManager) throws MalformedURLException {
        super(new URL[]{file.toURI().toURL()}, PreBotMain.class.getClassLoader());
        this.description = description;
        this.pluginManager = pluginManager;
    }

    public Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }

    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        return loadClass(name, resolve, true);
    }

    public Class<?> loadClass(String name, boolean resolve, boolean lookOtherPlugins) throws ClassNotFoundException {
        try {
            return super.loadClass(name, resolve);
        } catch (ClassNotFoundException ignored) {
        }
        //look in other plugins
        if (lookOtherPlugins) {
            //sorts the plugins that it depends/softdepend on first before other plugins
            List<Plugin> pluginsSorted = getPluginManager().getPlugins().stream().sorted((o1, o2) -> {
                if (getDescription().getDependencies().contains(o1.getDescription().getName()) || getDescription().getSoftDependencies().contains(o1.getDescription().getName()))
                    return -1;
                else return 0;
            }).collect(Collectors.toList());
            for (Plugin plugin : pluginsSorted) {
                if (plugin.getClassLoader() instanceof JarPluginClassLoader) {
                    JarPluginClassLoader loader = (JarPluginClassLoader) plugin.getClassLoader();
                    try {
                        Class<?> result = loader.loadClass(name, resolve, false);
                        if (!getDescription().getDependencies().contains(plugin.getDescription().getName()) && !getDescription().getSoftDependencies().contains(plugin.getDescription().getName()) && !loadedNonDepend.contains(plugin.getDescription().getName())) {
                            LOGGER.warn("{} loaded class {} from {} and it does not depend/softdepend on it", description.getName(), name, plugin.getDescription().getName());
                            loadedNonDepend.add(plugin.getDescription().getName());
                        }
                        return result;
                    } catch (ClassNotFoundException ignored) {
                    }
                }
            }
        }
        throw new ClassNotFoundException();
    }
}
