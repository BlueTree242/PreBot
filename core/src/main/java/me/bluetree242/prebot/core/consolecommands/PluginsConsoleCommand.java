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
import me.bluetree242.prebot.api.color.TextColor;
import me.bluetree242.prebot.api.commands.console.ConsoleCommandResponder;
import me.bluetree242.prebot.api.plugin.Plugin;
import me.bluetree242.prebot.core.command.console.PreBotConsoleCommand;

import java.util.StringJoiner;

public class PluginsConsoleCommand extends PreBotConsoleCommand {
    private final PreBot core;

    public PluginsConsoleCommand(PreBot core) {
        super("plugins", "Get plugins installed on Prebot", "", "pl");
        this.core = core;
    }

    @Override
    public void execute(String label, String[] args, ConsoleCommandResponder responder) {
        StringJoiner joiner = new StringJoiner(", ", "", "");
        for (Plugin plugin : core.getPluginManager().getPlugins()) {
            joiner.add((plugin.isEnabled() ? TextColor.GREEN : TextColor.RED) + plugin.getDescription().getName() + TextColor.RESET);
        }
        responder.send(TextColor.RESET + "Plugins (" + core.getPluginManager().getPlugins().size() + "): " + joiner);
    }
}
