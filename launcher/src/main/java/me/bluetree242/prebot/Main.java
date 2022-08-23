package me.bluetree242.prebot;

import me.bluetree242.prebot.api.LoggerProvider;
import me.bluetree242.prebot.core.PreBotMain;
import org.slf4j.Logger;

import java.nio.file.Paths;

public class Main {
    static {
        LoggerProvider.setProvider(new LoggerProvider() {
            @Override
            public Logger getLogger(Class<?> clz) {
                return org.slf4j.LoggerFactory.getLogger(clz);
            }
        });
    }

    public static void main(String[] args) {
        new PreBotMain(Paths.get("."));
    }
}
