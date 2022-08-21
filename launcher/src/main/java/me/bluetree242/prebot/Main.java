package me.bluetree242.prebot;

import me.bluetree242.prebot.core.PreBotMain;

import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        new PreBotMain(Paths.get("."));
    }
}
