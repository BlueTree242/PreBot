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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.bluetree242.jdaeventer.annotations.CustomEvent;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Custom event called when registering commands to a guild
 */
@CustomEvent
@RequiredArgsConstructor
public class GuildCommandsPreRegistrationEvent {
    private final CommandListUpdateAction action;
    private final List<CommandData> commands = new ArrayList<>();
    @Getter
    private final Guild guild;

    /**
     * Adds commands to be registered in the guild, make sure to check you will not hit the limit of 100 slash commands, 10 user commands and 10 context commands.
     *
     * @param data data to add
     * @return this instance, for chaining
     */
    public GuildCommandsPreRegistrationEvent addCommands(CommandData... data) {
        action.addCommands(data);
        Collections.addAll(commands, data);
        return this;
    }

    /**
     * Adds commands to be registered in the guild, make sure to check you will not hit the limit of 100 slash commands, 10 user commands and 10 context commands.
     *
     * @param data data to add
     * @return this instance, for chaining
     */
    public GuildCommandsPreRegistrationEvent addCommands(Collection<CommandData> data) {
        action.addCommands(data);
        commands.addAll(data);
        return this;
    }

    /**
     * Get all commands to be registered in this guild. <strong>NOTE:</strong> adding commands to this list will not register them
     *
     * @return commands to be registered in the guild
     */
    public List<CommandData> getCommands() {
        return commands;
    }
}
