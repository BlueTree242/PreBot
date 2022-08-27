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
import me.bluetree242.prebot.api.PreBot;
import me.bluetree242.prebot.api.commands.console.ConsoleCommand;
import me.bluetree242.prebot.api.commands.console.ConsoleCommandManager;
import me.bluetree242.prebot.api.commands.console.ConsoleCommandResponder;

import java.util.*;

@RequiredArgsConstructor
public class MainConsoleCommandManager implements ConsoleCommandManager {
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
            ConsoleCommand command = commandsMap.get(label);
            ConsoleCommandResponder responder = new ConsoleCommandResponder(command);
            if (command != null) {
                command.execute(label, args, responder); //execute the command
            } else {
                responder.send("Unknown Command.");
            }
        });
    }

    @Override
    public void registerCommands(ConsoleCommand... commands) {
        for (ConsoleCommand command : commands) {
            this.commands.add(command);
            for (String alias : command.getAliases()) {
                commandsMap.put(alias, command);
            }
            commandsMap.put(command.getName(), command);
            //TODO: add plugin prefix
        }
    }

}
