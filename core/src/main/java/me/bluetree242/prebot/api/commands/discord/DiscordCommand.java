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

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a Discord Command, Can be context or Slash.
 */
public interface DiscordCommand {

    /**
     * The type of the command
     * @return the type of the command
     */
    @NotNull
    default Command.Type getType() {
        return getData().getType();
    }

    /**
     * The command data for this command
     * @return the command data
     */
    CommandData getData();

    /**
     * If this command is admin command. Admin Commands are commands only available for Admin guilds & Admin Users
     * @return true if command is admin, false otherwise
     */
    boolean isAdmin();

    /**
     * Check if command be registered in this guild. returns the opposite of {@link DiscordCommand#isAdmin()} by default
     * @param guild Guild to check
     * @return true if the command should be registered there, false otherwise.
     */
    default boolean canRegister(@NotNull Guild guild) {
        return true;
    }

    /**
     * Called when this command was executed
     * @param event the event
     */
    void onCommand(@NotNull GenericCommandInteractionEvent event);
}
