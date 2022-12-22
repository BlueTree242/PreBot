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

package me.bluetree242.prebot.spigot;

import lombok.Getter;
import me.bluetree242.prebot.api.PreBot;
import me.bluetree242.prebot.api.color.TextColor;
import me.bluetree242.prebot.api.commands.console.ConsoleCommand;
import me.bluetree242.prebot.api.commands.console.ConsoleCommandResponder;
import me.bluetree242.prebot.core.PreBotMain;
import me.bluetree242.prebot.platform.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Consumer;

public class SpigotCoreImpl extends Platform implements SpigotCore{
    static {
        ClassLoader oldCl = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(SpigotCoreImpl.class.getClassLoader());
        try {
            Method method = LoggerFactory.class.getDeclaredMethod("bind");
            method.setAccessible(true);
            method.invoke(null);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        } finally {
            Thread.currentThread().setContextClassLoader(oldCl);
        }
    }

    private PreBotMain prebot;
    @Getter
    private final SpigotPluginDaemon plugin;
    public SpigotCoreImpl(SpigotPluginDaemon d) {
        this.plugin = d;
        Platform.setPlatform(this);
        prebot = new PreBotMain(d.getDataFolder().toPath());
    }
    @Override
    public void onEnable() {
        prebot.start();
        if (!prebot.isStarted()) prebot = null;
    }

    @Override
    public void onDisable() {
        if (prebot != null) prebot.stop();
        prebot = null;
    }

    @Override
    public void executeConsoleCommand(String cmd, Consumer<String> respond) {
        if (prebot == null || !prebot.isStarted() || prebot.isStopped()) {
            respond.accept(TextColor.RED + "PreBot is not available for commands at the moment.");
        } else prebot.getConsoleCommandManager().executeConsoleCommand(cmd, c -> new Responder(this, c, respond));
    }

    public static class Responder extends ConsoleCommandResponder {
        private final Consumer<String> respond;
        private final SpigotCoreImpl core;
        public Responder(SpigotCoreImpl core, ConsoleCommand command, Consumer<String> respond) {
            super(command);
            this.respond = respond != null ? respond : r -> core.getPlugin().sendConsole(r);
            this.core = core;
        }

        @Override
        public void send(String toSend) {
            respond.accept(toSend);
        }
    }


    @Override
    public PlatformType getType() {
        return PlatformType.SPIGOT;
    }

    @Override
    public Logger getLogger(Class<?> clz) {
        return LoggerFactory.getLogger(clz);
    }

    @Override
    public PreBot getPreBot() {
        return prebot;
    }
}
