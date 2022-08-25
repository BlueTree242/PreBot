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
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

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
    @Getter
    private final GatewayIntent[] requiredIntents;
    @Getter
    private final CacheFlag[] requiredCacheFlags;

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
        if (yml.containsKey("required-intents") && !(yml.get("required-intents") instanceof List)) throw new InvalidPluginException("Required intents must be a list of strings.");
        if (yml.containsKey("required-intents")) requiredIntents = ((List<String>) yml.get("required-intents")).stream().map(i -> {
            try {
                return GatewayIntent.valueOf(i.toUpperCase(Locale.ROOT));
            } catch (Exception x) {
                throw new InvalidPluginException("Intent " + i + " is not valid!");
            }
        }).toArray(GatewayIntent[]::new);
        else requiredIntents = new GatewayIntent[0];
        if (yml.containsKey("required-cache-flags") && !(yml.get("required-cache-flags") instanceof List)) throw new InvalidPluginException("Required Cache Flags must be a list of strings.");
        if (yml.containsKey("required-cache-flags")) requiredCacheFlags = ((List<String>) yml.get("required-intents")).stream().map(c -> {
            try {
                return CacheFlag.valueOf(c.toUpperCase(Locale.ROOT));
            } catch (Exception x) {
                throw new InvalidPluginException("Cache flag " + c + " is not valid!");
            }
        }).toArray(CacheFlag[]::new);
        else requiredCacheFlags = new CacheFlag[0];
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
