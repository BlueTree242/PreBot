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
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
    }
    @Override
    public void onEnable() {
        prebot = new PreBotMain(plugin.getDataFolder().toPath());
        prebot.start();
        if (!prebot.isStarted()) prebot = null;
    }

    @Override
    public void onDisable() {
        if (prebot != null && !prebot.isStopped()) prebot.stop();
    }

    @Override
    public void executeConsoleCommand(String cmd, Consumer<String> respond) {
        if (prebot == null || !prebot.isStarted() || prebot.isStopped()) {
            respond.accept(TextColor.RED + "PreBot is not available for commands at the moment.");
        } else prebot.getConsoleCommandManager().executeConsoleCommand(cmd, c -> new Responder(this, c, respond));
    }

    @Override
    public List<String> getConsoleCommands() {
        if (prebot == null || !prebot.isStarted() || prebot.isStopped()) return Collections.emptyList();
        return prebot.getConsoleCommandManager().getCommands().stream().map(ConsoleCommand::getName).collect(Collectors.toList());
    }

    public static class Responder extends ConsoleCommandResponder {
        private final Consumer<String> respond;

        public Responder(SpigotCoreImpl core, ConsoleCommand command, Consumer<String> respond) {
            super(command);
            this.respond = respond != null ? respond : r -> core.getPlugin().sendConsole(r);
        }

        @Override
        public void send(String toSend) {
            respond.accept(toSend);
        }
    }

    @Override
    public ConsoleCommandResponder getConsoleCommandResponder(ConsoleCommand cmd) {
        return new Responder(this, cmd, null);
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

    @Override
    public void onClose(CloseReason reason, Throwable ex) {
        switch (reason) {
            case STOP:
            case OTHER:
                plugin.setCloseReason("§cPreBot was stopped.");
                break;
            case UNKNOWN:
                plugin.setCloseReason("§cPreBot was stopped by an unknown reason.");
            case BAD_CONFIG:
                plugin.setCloseReason("§cPreBot was stopped due to bad configuration. Check logs for more details.");
                break;
            case INVALID_TOKEN:
                plugin.setCloseReason("§cPreBot was stopped due to invalid bot token, Please enter a valid bot token into your config.yml (§8plugins/PreBot/config.yml§c) and restart the server.");
        }
        if (plugin.isEnabled()) plugin.disable();
    }

    @Override
    public void onStop() {
        if (plugin.isEnabled()) plugin.disable();
    }

    @Override
    public String getHelpCommandHeader(String label) {
        return TextColor.GREEN + "Listing all commands, run \"/prebot " + label + " <command>\" to get help for a specific command";
    }

    @Override
    public String getUnknownCommandMessage() {
        return "Unknown Command. Type \"/prebot ?\" for list of existing commands.";
    }
}
