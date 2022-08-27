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

package me.bluetree242.prebot.api.commands.console;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a console command.
 */
public interface ConsoleCommand {
    /**
     * The name of the command
     * @return the name of the command
     */
    @NotNull String getName();

    /**
     * Other names this command owns.
     * @return Aliases of the command.
     */
    @NotNull String[] getAliases();

    /**
     * The description of the command
     * @return text that describes the command, might be null.
     */
    @Nullable String getDescription();

    /**
     * Called when this command is executed
     * @param label label used to execute the command
     * @param args command arguments.
     * @param responder the responder attached to this event
     */
    void execute(String label, String[] args, ConsoleCommandResponder responder);
}
