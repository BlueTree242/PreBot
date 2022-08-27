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
import me.bluetree242.prebot.api.PreBotVersion;
import me.bluetree242.prebot.api.commands.console.ConsoleCommandResponder;
import me.bluetree242.prebot.api.plugin.Plugin;
import me.bluetree242.prebot.core.command.console.PreBotConsoleCommand;

public class VersionConsoleCommand extends PreBotConsoleCommand {
    private final PreBot core;
    public VersionConsoleCommand(PreBot core) {
        super("version", "Get the version of PreBot, or a plugin", "ver");
        this.core = core;
    }

    @Override
    public void execute(String label, String[] args, ConsoleCommandResponder responder) {
        if (args.length == 0)
        responder.send("Running PreBot " + PreBotVersion.VERSION);
        else {
            String name = args[0];
            Plugin plugin = core.getPluginManager().getPluginByName(name);
            if (plugin == null) {
                responder.send("This plugin was not found");
            } else {
                responder.send("Name: " + plugin.getDescription().getName());
                responder.send("Version: " + plugin.getDescription().getVersion());
                responder.send("Authors: " + String.join(", ", plugin.getDescription().getAuthors()));
            }
        }
    }
}
