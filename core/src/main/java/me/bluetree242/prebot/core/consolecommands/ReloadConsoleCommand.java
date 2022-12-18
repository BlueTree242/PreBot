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

package me.bluetree242.prebot.core.consolecommands;

import me.bluetree242.prebot.api.LoggerProvider;
import me.bluetree242.prebot.api.PreBot;
import me.bluetree242.prebot.api.color.TextColor;
import me.bluetree242.prebot.api.commands.console.ConsoleCommandResponder;
import me.bluetree242.prebot.api.plugin.Plugin;
import me.bluetree242.prebot.core.command.console.PreBotConsoleCommand;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Set;

public class ReloadConsoleCommand extends PreBotConsoleCommand {
    private static final Logger LOGGER = LoggerProvider.getLogger(ReloadConsoleCommand.class);
    private final PreBot core;

    public ReloadConsoleCommand(PreBot core) {
        super("reload", "Reloads PreBot or a plugin", "[plugin|all]", "rl");
        this.core = core;
    }

    @Override
    public void execute(String label, String[] args, ConsoleCommandResponder responder) {
        if (args.length == 0) {
            core.reload();
            responder.send(TextColor.GREEN + "Reloaded PreBot successfully");
        } else {
            String name = args[0];
            if (name.equalsIgnoreCase("all")) {
                Set<String> failed = new HashSet<>();
                for (Plugin plugin : core.getPluginManager().getPlugins()) {
                    try {
                        if (plugin.isEnabled()) plugin.reload();
                    } catch (Exception e) {
                        LOGGER.error("Failed to reload plugin " + plugin.getDescription().getName(), e);
                        failed.add(plugin.getDescription().getName());
                    }
                }
                if (failed.isEmpty()) {
                    responder.send(TextColor.GREEN + "Successfully reloaded all plugins");
                } else {
                    responder.send(TextColor.RED + "Reloaded all plugins but " + failed.size() + " plugins failed: " + TextColor.YELLOW + String.join(TextColor.RESET + ", " + TextColor.YELLOW, failed));
                }
            } else {
                Plugin plugin = core.getPluginManager().getPluginByName(name);
                if (plugin == null) {
                    responder.send(TextColor.RED + "Plugin not found");
                } else {
                    if (!plugin.isEnabled()) {
                        responder.send(TextColor.RED + "Plugin is disabled");
                        return;
                    }
                    plugin.reload();
                    responder.send(TextColor.GREEN + "Successfully reloaded " + plugin.getDescription().getName());
                }
            }
        }
    }
}
