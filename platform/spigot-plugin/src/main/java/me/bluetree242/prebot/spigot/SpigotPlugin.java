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

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.LogRecord;

public class SpigotPlugin extends JavaPlugin implements SpigotPluginDaemon, CommandExecutor {
    private SpigotCore core;

    @Override
    public void onEnable() {
        Objects.requireNonNull(getServer().getPluginCommand("prebot")).setExecutor(this);
        core.onEnable();
    }

    @Override
    public void onDisable() {
        core.onDisable();
    }

    @SneakyThrows
    @Override
    public void onLoad() {
        Path jarPath = getDataFolder().toPath().resolve("cache").resolve("spigot-core.jar");
        jarPath.getParent().toFile().mkdirs();
        Files.copy(Objects.requireNonNull(getResource("spigot-core.jarx")), jarPath, StandardCopyOption.REPLACE_EXISTING);
        CoreClassLoader loader = new CoreClassLoader(jarPath.toUri().toURL());
        Class<SpigotCore> core = (Class<SpigotCore>) loader.loadClass("me.bluetree242.prebot.spigot.SpigotCoreImpl");
        this.core = core.getConstructor(SpigotPluginDaemon.class).newInstance(this);
    }

    @Override
    public void log(LogRecord record) {
        record.setLoggerName(getLogger().getName());
        getLogger().log(record);
    }

    @Override
    public void sendConsole(String msg) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('§', msg));
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Consumer<String> responder = output -> Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('§', output));
        if (args.length == 0) {
            core.executeConsoleCommand("ver", responder);
        } else {
            if (!sender.hasPermission("prebot.command")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use this.");
                return true;
            }
            String[] modifiedArray = Arrays.copyOfRange(args, 1, args.length);
            core.executeConsoleCommand(args[0] + " " + String.join(" ", modifiedArray), responder);
        }
        return true;
    }
}
