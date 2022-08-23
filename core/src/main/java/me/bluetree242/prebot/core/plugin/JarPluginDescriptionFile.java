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
import me.bluetree242.prebot.api.exceptions.plugin.InvalidPluginException;
import me.bluetree242.prebot.api.plugin.PluginDescription;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class JarPluginDescriptionFile implements PluginDescription, Comparable<JarPluginDescriptionFile> {
    @Getter
    private final String name;
    @Getter
    private final String version;
    @Getter
    private final List<String> authors;
    @Getter
    private final String main;
    @Getter
    private final List<String> dependencies;
    @Getter
    private final List<String> softDependencies;
    @Getter
    private final File jarFile;

    @SuppressWarnings("unchecked")
    public JarPluginDescriptionFile(Map<String, Object> yml, File jarFile) {
        this.jarFile = jarFile;
        require("name", yml, String.class);
        require("version", yml, String.class);
        require("authors", yml, List.class);
        require("main", yml, String.class);
        name = (String) yml.get("name");
        version = (String) yml.get("version");
        authors = Collections.unmodifiableList((List<String>) yml.get("authors"));
        if (authors.isEmpty()) throw new InvalidPluginException("Authors cannot be empty");
        main = (String) yml.get("main");
        dependencies = Collections.unmodifiableList(yml.containsKey("dependencies") ? (List<String>) yml.get("dependencies") : new ArrayList<>());
        softDependencies = Collections.unmodifiableList(yml.containsKey("softdependencies") ? (List<String>) yml.get("softdependencies") : new ArrayList<>());
    }

    private static boolean checkExists(String value, Map<String, Object> yml, Class<?> type) {
        return yml.containsKey(value) && type.isInstance(yml.get(value));
    }

    private static void require(String value, Map<String, Object> yml, Class<?> type) {
        if (!checkExists(value, yml, type))
            throw new InvalidPluginException(value + " is not found in prebot.yml or has the wrong type");
    }

    @Override
    public int compareTo(@NotNull JarPluginDescriptionFile plugin) {
        if (plugin.getDependencies().contains(name) || plugin.getSoftDependencies().contains(name)) return -1;
        else if (getDependencies().contains(plugin.getName()) || getSoftDependencies().contains(plugin.getName()))
            return 1;
        return 0;
    }
}
