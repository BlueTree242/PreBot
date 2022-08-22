package me.bluetree242.prebot.core.plugin;

import lombok.Getter;
import me.bluetree242.prebot.exceptions.InvalidPluginException;
import me.bluetree242.prebot.plugin.PluginDescription;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class JarPluginDescriptionFile implements PluginDescription, Comparable {
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
    public int compareTo(@NotNull Object o) {
        if (!(o instanceof JarPluginDescriptionFile)) throw new IllegalArgumentException();
        JarPluginDescriptionFile plugin = (JarPluginDescriptionFile) o;
        if (plugin.getDependencies().contains(name) || plugin.getSoftDependencies().contains(name)) return -1;
        else if (getDependencies().contains(plugin.getName()) || getSoftDependencies().contains(plugin.getName()))
            return 1;
        return 0;
    }
}
