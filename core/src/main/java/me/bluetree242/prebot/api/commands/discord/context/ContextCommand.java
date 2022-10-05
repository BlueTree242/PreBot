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

package me.bluetree242.prebot.api.commands.discord.context;

import me.bluetree242.jdaeventer.objects.EventInformation;
import me.bluetree242.prebot.api.commands.discord.DiscordCommand;
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.GenericContextInteractionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link DiscordCommand}, but as a context command
 */
public interface ContextCommand<T, E extends GenericContextInteractionEvent<T>> extends DiscordCommand {

    /**
     * Called when the context command is used
     *
     * @param event context command event
     */
    void onCommand(E event, EventInformation info);

    /**
     * This redirects to {@link ContextCommand#onCommand} <strong>DO NOT OVERRIDE THIS</strong>
     *(GenericContextInteractionEvent, EventInformation)
     * @param event the event
     */
    @SuppressWarnings("unchecked")
    @Override
    default void onCommand(@NotNull GenericCommandInteractionEvent event, EventInformation info) {
        onCommand((E) event, info);
    }
}
