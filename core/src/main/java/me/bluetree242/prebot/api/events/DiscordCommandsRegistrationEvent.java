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

package me.bluetree242.prebot.api.events;

import lombok.RequiredArgsConstructor;
import me.bluetree242.jdaeventer.annotations.CustomEvent;
import me.bluetree242.prebot.api.commands.discord.result.CommandRegistrationResult;

import java.util.List;

/**
 * This is a custom event, called right after registering some discord commands to some guilds
 */
@CustomEvent
@RequiredArgsConstructor
public class DiscordCommandsRegistrationEvent {
    private final List<CommandRegistrationResult> results;

    /**
     * Get the results of this registration
     *
     * @return the results of this registration
     */
    public List<CommandRegistrationResult> getResults() {
        return results;
    }
}
