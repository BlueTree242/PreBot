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
import me.bluetree242.prebot.api.commands.console.ConsoleCommandResponder;
import me.bluetree242.prebot.core.command.console.PreBotConsoleCommand;

public class StopConsoleCommand extends PreBotConsoleCommand {
    private final PreBot core;
    public StopConsoleCommand(PreBot core) {
        super("stop", "Stops PreBot");
        this.core = core;
    }

    @Override
    public void execute(String label, String[] args, ConsoleCommandResponder responder) {
        if (!core.isStopped()) core.stop();
    }
}
