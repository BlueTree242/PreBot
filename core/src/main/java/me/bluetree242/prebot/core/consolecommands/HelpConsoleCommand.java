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

import me.bluetree242.prebot.api.PreBot;
import me.bluetree242.prebot.api.commands.console.ConsoleCommand;
import me.bluetree242.prebot.api.commands.console.ConsoleCommandResponder;
import me.bluetree242.prebot.api.plugin.Plugin;
import me.bluetree242.prebot.api.plugin.commands.console.PluginConsoleCommand;
import me.bluetree242.prebot.core.command.console.PreBotConsoleCommand;

import java.util.Locale;

public class HelpConsoleCommand extends PreBotConsoleCommand {
    private final PreBot core;

    public HelpConsoleCommand(PreBot core) {
        super("help", "Get help with a command or list all existing console commands.", "[command]", "?");
        this.core = core;
    }

    @Override
    public void execute(String label, String[] args, ConsoleCommandResponder responder) {
        if (args.length != 0) {
            String cmd = args[0];
            ConsoleCommand command = core.getConsoleCommandManager().getCommandsMap().get(cmd.toLowerCase(Locale.ROOT));
            if (command == null) {
                responder.send("This command does not exist");
            } else {
                responder.send("Name: §r" + command.getName());
                responder.send("Description: §r" + command.getDescription());
                if (command.getUsage() != null) {
                    responder.send("Usage: §r" + cmd + " " + command.getUsage());
                }
                if (command instanceof PluginConsoleCommand) {
                    Plugin plugin = ((PluginConsoleCommand) command).getPlugin();
                    responder.send("Plugin: §r" + plugin);
                }
            }
            return;
        }
        responder.send("Listing all commands, run \"" + label + " <command> to get help for a specific command");
        for (ConsoleCommand command : core.getConsoleCommandManager().getCommands()) {
            responder.send(command.getName() + ": §r" + command.getDescription());
        }
    }
}
