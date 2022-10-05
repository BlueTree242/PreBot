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

package me.bluetree242.prebot.api.commands.discord.slash;

import me.bluetree242.jdaeventer.objects.EventInformation;
import me.bluetree242.prebot.api.commands.discord.DiscordCommand;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link DiscordCommand}, but as a slash command
 */
public interface SlashCommand extends DiscordCommand {

    SlashCommandData getData();

    /**
     * Called when the slash command is used
     *
     * @param event slash command event
     */
    void onCommand(SlashCommandInteractionEvent event, EventInformation info);

    /**
     * This redirects to {@link SlashCommand#onCommand(SlashCommandInteractionEvent, EventInformation)} <strong>DO NOT OVERRIDE THIS</strong>
     *
     * @param event the event
     */
    @Override
    default void onCommand(@NotNull GenericCommandInteractionEvent event, EventInformation info) {
        onCommand((SlashCommandInteractionEvent) event, info);
    }

    /**
     * Called when auto complete is requested for this command
     *
     * @param event the event
     * @param info event information associated with this event
     */
    default void onAutoComplete(CommandAutoCompleteInteractionEvent event, EventInformation info) {
        //by default, nothing
    }
}
