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

import java.nio.file.Paths;

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
        TerminalConsoleAppender.isAnsiSupported(); //this initializes terminal
        Thread thread = new Thread(() -> new Main().start());
        thread.setDaemon(true);
        thread.setName("PreBot-Command-Listener");
        thread.start(); //start listening for commands
        PreBotMain prebot = new PreBotMain(System.getenv("bot.root") == null ? Paths.get(".") : Paths.get(System.getenv("bot.root")));
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
