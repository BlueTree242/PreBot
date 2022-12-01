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

package me.bluetree242.prebot;

import lombok.SneakyThrows;
import me.bluetree242.prebot.api.LoggerProvider;
import me.bluetree242.prebot.api.PreBot;
import me.bluetree242.prebot.core.PreBotMain;
import net.minecrell.terminalconsole.SimpleTerminalConsole;
import net.minecrell.terminalconsole.TerminalConsoleAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Paths;
import java.util.Arrays;

public class Main extends SimpleTerminalConsole {
    static {
        LoggerProvider.setProvider(new LoggerProvider() {
            @Override
            public Logger getLogger(Class<?> clz) {
                return LoggerFactory.getLogger(clz);
            }
        });
    }

    public static void main(String[] args) {
        if ((args.length == 0) || Arrays.stream(args).noneMatch(s -> s.equalsIgnoreCase("-nogui"))) showGUI();
        TerminalConsoleAppender.isAnsiSupported(); //this initializes terminal
        Thread thread = new Thread(() -> new Main().start());
        thread.setDaemon(true);
        thread.setName("PreBot-Command-Listener");
        thread.start(); //start listening for commands
        PreBotMain prebot = new PreBotMain(System.getenv("bot.root") == null ? Paths.get(".") : Paths.get(System.getenv("bot.root")));
    }

    private static void showGUI() { //Temporary GUI to help prevent running bot from jar
        if (GraphicsEnvironment.isHeadless()) return;
        String[] text = new String[]
                {
                        "PreBot detected possible problems ",
                        "There is a chance you accidentally double-clicked the jar instead of using a start script",
                        "Which means if PreBot starts you wouldn't be able to see console or control it.",
                        "If you are 100% sure you are using a script, you can add -nogui to the end of your command.",
                        "PreBot is currently not ON until you do the previous."
                };
        //also a console message
        System.out.println("If you can see this it means that you are able to see the console and probably control it, to start prebot please add -nogui to your startup command.");
        final Runnable runnable =
                (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.exclamation");
        if (runnable != null) runnable.run();
        JOptionPane.showOptionDialog(
                null,
                String.join("\n", text),
                "PreBot cannot start", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null);
        System.exit(-1);
    }

    @Override
    protected boolean isRunning() {
        if (PreBot.getInstance() == null) return true;
        return !PreBot.getInstance().isStopped();
    }

    @SneakyThrows
    @Override
    protected void runCommand(String command) {
        if (PreBot.getInstance() == null) return;
        while (!PreBot.getInstance().isStarted()) Thread.sleep(100); //wait for prebot to start
        PreBot.getInstance().getConsoleCommandManager().executeConsoleCommand(command);
    }

    @SneakyThrows
    @Override
    protected void shutdown() {
        if (PreBot.getInstance() == null || PreBot.getInstance().isStopped()) return;
        while (!PreBot.getInstance().isStarted()) Thread.sleep(100); //wait for prebot to start
        PreBot.getInstance().stop();
    }
}
