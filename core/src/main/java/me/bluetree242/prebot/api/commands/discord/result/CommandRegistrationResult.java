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

package me.bluetree242.prebot.api.commands.discord.result;


import me.bluetree242.prebot.api.commands.discord.DiscordCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.Set;

/**
 * Represents a Command Registration result in a guild. Can be success or fail
 */
public class CommandRegistrationResult {
    private final Guild guild;
    private final Set<CommandData> commandDatas;
    private final Set<DiscordCommand> commands;
    private final boolean failed;
    private final Throwable exception;

    public CommandRegistrationResult(Guild guild, Set<CommandData> commandDatas, Set<DiscordCommand> commands) {
        this.guild = guild;
        this.commandDatas = commandDatas;
        this.commands = commands;
        failed = false;
        exception = null;
    }

    public CommandRegistrationResult(Guild guild, Set<CommandData> commandDatas, Set<DiscordCommand> commands, Throwable ex) {
        this.guild = guild;
        this.commandDatas = commandDatas;
        this.commands = commands;
        failed = true;
        exception = ex;
    }

    /**
     * The guild where this result has happened
     *
     * @return the guild
     */
    public Guild getGuild() {
        return guild;
    }

    /**
     * The command data set that is registered in the guild
     *
     * @return the command data set which is registered in the guild
     */
    public Set<CommandData> getCommandDatas() {
        return commandDatas;
    }

    /**
     * The Discord Commands that is registered in the guild
     *
     * @return Discord commands registered in guild
     */
    public Set<DiscordCommand> getCommands() {
        return commands;
    }

    /**
     * If this registration attempt failed
     *
     * @return true if the registration failed, false otherwise
     */
    public boolean isFailed() {
        return failed;
    }

    /**
     * The exception in case of failure, null if {@link CommandRegistrationResult#isFailed()} is false
     *
     * @return the exception in case of failure, null otherwise.
     * @see CommandRegistrationResult#isFailed()
     */
    public Throwable getException() {
        return exception;
    }
}
