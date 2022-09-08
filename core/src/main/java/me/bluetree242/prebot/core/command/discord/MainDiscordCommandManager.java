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

package me.bluetree242.prebot.core.command.discord;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.bluetree242.prebot.api.PreBot;
import me.bluetree242.prebot.api.commands.discord.DiscordCommand;
import me.bluetree242.prebot.api.commands.discord.DiscordCommandManager;
import me.bluetree242.prebot.api.commands.discord.SlashCommand;
import net.dv8tion.jda.api.interactions.commands.Command;

import java.util.*;

@RequiredArgsConstructor
public class MainDiscordCommandManager implements DiscordCommandManager {
    private final PreBot core;
    @Getter
    private final Set<DiscordCommand> commands = new HashSet<>();
    @Getter
    private final Map<String, DiscordCommand> messageCommands = new HashMap<>();
    @Getter
    private final Map<String, DiscordCommand> userCommands = new HashMap<>();
    @Getter
    private final Map<String, SlashCommand> slashCommands = new HashMap<>();

    @Override
    public void registerCommands(DiscordCommand... cmds) {
        Collections.addAll(commands, cmds);
        for (DiscordCommand cmd : cmds) {
            if (cmd.getType() == Command.Type.SLASH && slashCommands.size() < 100)
                slashCommands.put(cmd.getData().getName(), (SlashCommand) cmd);
            else if (cmd.getType() == Command.Type.MESSAGE && messageCommands.size() < 10)
                messageCommands.put(cmd.getData().getName(), cmd);
            else if (cmd.getType() == Command.Type.USER && userCommands.size() < 10)
                userCommands.put(cmd.getData().getName(), cmd);
        }
    }
}
