package me.bluetree242.prebot.core.plugin;

import lombok.Getter;
import me.bluetree242.prebot.exceptions.InvalidPluginException;
import me.bluetree242.prebot.plugin.PluginDescriptionFile;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class JarPluginDescriptionFile implements PluginDescriptionFile {
    @Getter
    private final String name;
    @Getter
    private final String version;
    @Getter
    private final List<String> authors;
    @Getter
    private final String main;

    @SuppressWarnings("unchecked")
    public JarPluginDescriptionFile(Map<String, Object> yml) {
        require("name", yml, String.class);
        require("version", yml, String.class);
        System.out.println(yml.get("authors"));
        require("authors", yml, List.class);
        require("main", yml, String.class);
        name = (String) yml.get("name");
        version = (String) yml.get("version");
        authors = Collections.unmodifiableList((List<String>) yml.get("authors"));
        main = (String) yml.get("main");
    }

    private static boolean checkExists(String value, Map<String, Object> yml, Class<?> type) {
        return yml.containsKey(value) && type.isInstance(yml.get(value));
    }

    private static void require(String value, Map<String, Object> yml, Class<?> type) {
        if (!checkExists(value, yml, type))
            throw new InvalidPluginException(value + " is not found in prebot.yml or has the wrong type");
    }
}
