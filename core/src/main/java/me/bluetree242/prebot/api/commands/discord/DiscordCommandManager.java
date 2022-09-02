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

package me.bluetree242.prebot.api.commands.discord;

import java.util.Map;
import java.util.Set;
import net.dv8tion.jda.api.interactions.commands.Command;

/**
 * Represents the Discord Command Manager
 */
public interface DiscordCommandManager {

    /**
     * Returns evert single command, even ones that cannot be used
     * @return all existing commands
     */
    Set<DiscordCommand> getCommands();

    /**
     * All slash commands, while key is the identifier, and value is the command
     * @return all slash commands that exist in discord
     */
    Map<String, SlashCommand> getSlashCommands();

    /**
     * All context commands of type {@link Command.Type#MESSAGE}
     * @return all message commands that exist in discord
     */
    Map<String, DiscordCommand> getMessageCommands();

    /**
     * All context commands of type {@link Command.Type#MESSAGE}
     * @return all user commands that exist in discord
     */
    Map<String, DiscordCommand> getUserCommands();

    /**
     * Registers commands and puts them to their correct locations according to user configuration
     * @param cmd commands to register
     */
    void registerCommands(DiscordCommand... cmd);
}
