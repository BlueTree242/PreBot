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

import me.bluetree242.prebot.api.PreBot;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

/**
 * This class controls console commands
 *
 * @see ConsoleCommandManager
 */
public interface ConsoleCommandManager {
    /**
     * Executes a console command, in {@link PreBot#getExecutor()}
     *
     * @param cmd exact command to execute, as if it was written in the console.
     */
    void executeConsoleCommand(String cmd);

    /**
     * The registered Commands
     *
     * @return the commands registered in this manager instance.
     * @see ConsoleCommandManager#registerCommands(ConsoleCommand...)
     */
    @NotNull
    Set<ConsoleCommand> getCommands();

    /**
     * The commands in this manager. The Map's key is name, and aliases, and even pluginname:command, this means values can be duplicated in this map.
     *
     * @return a map of all labels, values can be repeated.
     * @see ConsoleCommandManager#registerCommands(ConsoleCommand...)
     */
    Map<String, ConsoleCommand> getCommandsMap();

    /**
     * Registers commands to the manager
     *
     * @param commands commands to register
     */
    void registerCommands(ConsoleCommand... commands);
}
