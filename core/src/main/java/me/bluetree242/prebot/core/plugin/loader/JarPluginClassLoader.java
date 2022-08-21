package me.bluetree242.prebot.core.plugin.loader;

import me.bluetree242.prebot.core.PreBotMain;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class JarPluginClassLoader extends URLClassLoader {
    public JarPluginClassLoader(File file) throws MalformedURLException {
        super(new URL[]{file.toURI().toURL()}, PreBotMain.class.getClassLoader());
    }

    public Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }
}
