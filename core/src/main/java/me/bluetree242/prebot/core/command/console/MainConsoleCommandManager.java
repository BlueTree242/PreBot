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

package me.bluetree242.prebot.core.command.console;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.bluetree242.prebot.api.LoggerProvider;
import me.bluetree242.prebot.api.PreBot;
import me.bluetree242.prebot.api.commands.console.ConsoleCommand;
import me.bluetree242.prebot.api.commands.console.ConsoleCommandManager;
import me.bluetree242.prebot.api.commands.console.ConsoleCommandResponder;
import me.bluetree242.prebot.api.plugin.commands.console.PluginConsoleCommand;
import org.slf4j.Logger;

import java.util.*;

@RequiredArgsConstructor
public class MainConsoleCommandManager implements ConsoleCommandManager {
    private static final Logger LOGGER = LoggerProvider.getProvider().getLogger(MainConsoleCommandManager.class);
    @Getter
    private final Set<ConsoleCommand> commands = new HashSet<>();
    @Getter
    private final Map<String, ConsoleCommand> commandsMap = new HashMap<>();
    private final PreBot core;

    @Override
    public void executeConsoleCommand(String cmd) {
        core.getExecutor().execute(() -> {
            String[] split = cmd.split(" ");
            String label = split[0];
            String[] args = Arrays.copyOfRange(split, 1, split.length);
            ConsoleCommand command = commandsMap.get(label.toLowerCase(Locale.ROOT));
            try {
                ConsoleCommandResponder responder = new ConsoleCommandResponder(command);
                if (command != null) {
                    if (command instanceof PluginConsoleCommand) {
                        if (!((PluginConsoleCommand) command).getPlugin().isEnabled())
                            throw new IllegalStateException("Plugin is disabled");
                    }
                    command.execute(label, args, responder); //execute the command
                } else {
                    responder.send("Unknown Command. Type \"?\" for list of existing commands.");
                }
            } catch (Exception ex) {
                LOGGER.error("An error occurred while executing console command \"" + cmd + "\"", ex);
            }
        });
    }

    @Override
    public void registerCommands(ConsoleCommand... commands) {
        for (ConsoleCommand command : commands) {
            this.commands.add(command);
            for (String alias : command.getAliases()) {
                commandsMap.put(alias.toLowerCase(Locale.ROOT), command);
            }
            commandsMap.put(command.getName().toLowerCase(Locale.ROOT), command);
            if (command instanceof PluginConsoleCommand) {
                commandsMap.put(((PluginConsoleCommand) command).getPlugin().getDescription().getName().toLowerCase(Locale.ROOT) + ":" + command.getName().toLowerCase(Locale.ROOT), command);
            }
        }
    }

}
