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
import me.bluetree242.jdaeventer.objects.EventInformation;
import me.bluetree242.prebot.api.LoggerProvider;
import me.bluetree242.prebot.api.PreBot;
import me.bluetree242.prebot.api.commands.discord.DiscordCommand;
import me.bluetree242.prebot.api.commands.discord.DiscordCommandManager;
import me.bluetree242.prebot.api.commands.discord.context.MessageContextCommand;
import me.bluetree242.prebot.api.commands.discord.context.UserContextCommand;
import me.bluetree242.prebot.api.commands.discord.result.CommandRegistrationResult;
import me.bluetree242.prebot.api.commands.discord.slash.SlashCommand;
import me.bluetree242.prebot.api.events.DiscordCommandsRegistrationEvent;
import me.bluetree242.prebot.api.events.GuildCommandsPreRegistrationEvent;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.slf4j.Logger;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MainDiscordCommandManager implements DiscordCommandManager {
    private static final Logger LOGGER = LoggerProvider.getProvider().getLogger(MainDiscordCommandManager.class);
    private final PreBot core;
    @Getter
    private final Set<DiscordCommand> commands = new HashSet<>();
    @Getter
    private final Map<String, MessageContextCommand> messageCommands = new HashMap<>();
    @Getter
    private final Map<String, UserContextCommand> userCommands = new HashMap<>();
    @Getter
    private final Map<String, SlashCommand> slashCommands = new HashMap<>();

    @Override
    public void registerCommands(DiscordCommand... cmds) {
        performCheck(cmds);
        Collections.addAll(commands, cmds);
        for (DiscordCommand cmd : cmds) {
            if (cmd.getType() == Command.Type.SLASH && slashCommands.size() < 100)
                slashCommands.put(cmd.getData().getName(), (SlashCommand) cmd);
            else if (cmd.getType() == Command.Type.MESSAGE && messageCommands.size() < 10)
                messageCommands.put(cmd.getData().getName(), (MessageContextCommand) cmd);
            else if (cmd.getType() == Command.Type.USER && userCommands.size() < 10)
                userCommands.put(cmd.getData().getName(), (UserContextCommand) cmd);
        }
    }

    private void performCheck(DiscordCommand... cmds) {
        int num = 0;
        for (DiscordCommand cmd : cmds) {
            num++;
            if (cmd.getType() == Command.Type.SLASH && !(cmd instanceof SlashCommand))
                throw new IllegalArgumentException("Command is type of SLASH, but not instance of SlashCommand, for command #" + num);
            if (cmd.getType() == Command.Type.USER && !(cmd instanceof UserContextCommand))
                throw new IllegalArgumentException("Command is type of USER, but not instance of UserContextCommand, for command #" + num);
            if (cmd.getType() == Command.Type.MESSAGE && !(cmd instanceof MessageContextCommand))
                throw new IllegalArgumentException("Command is type of MESSAGE, but not instance of MessageContextCommand, for command #" + num);
        }
    }

    @Override
    public void registerCommands(Collection<Guild> guilds) {
        Set<RestAction<CommandRegistrationResult>> actions = new HashSet<>();
        for (Guild guild : guilds) {
            Set<DiscordCommand> commands = new HashSet<>();
            commands.addAll(core.getDiscordCommandManager().getMessageCommands().values());
            commands.addAll(core.getDiscordCommandManager().getUserCommands().values());
            commands.addAll(core.getDiscordCommandManager().getSlashCommands().values());
            commands = commands.stream().filter(c -> c.canRegister(guild)).filter(c -> {
                if (core.isAdmin(guild)) return true;
                else return !c.isAdmin();
            }).collect(Collectors.toSet());
            Set<CommandData> data = commands.stream().map(DiscordCommand::getData).collect(Collectors.toSet());
            Set<DiscordCommand> finalCommands = commands;
            CommandListUpdateAction action = guild.updateCommands();
            GuildCommandsPreRegistrationEvent event = new GuildCommandsPreRegistrationEvent(action, guild).addCommands(data);
            EventInformation info = new EventInformation(core.getEventer(), event);
            core.getEventer().fireEvent(event, info);
            if (!info.isMarkedCancelled()) actions.add(
                    action
                            .timeout(10, TimeUnit.SECONDS)
                            .map(r -> new CommandRegistrationResult(guild, data, finalCommands))
                            .onErrorMap(er -> new CommandRegistrationResult(guild, data, finalCommands, er))
            );
        }
        if (actions.isEmpty()) return;
        RestAction.allOf(actions).queue(s -> {
            core.getEventer().fireEvent(new DiscordCommandsRegistrationEvent(s));
            Set<CommandRegistrationResult> failures = s.stream().filter(CommandRegistrationResult::isFailed).collect(Collectors.toSet());
            if (failures.isEmpty()) return; //no failures
            long maxCreateReached = failures.stream()
                    .filter(r -> r.getException() instanceof ErrorResponseException && ((ErrorResponseException) r.getException()).getErrorCode() == 30034).count();
            long timeout = failures.stream()
                    .filter(r -> r.getException() instanceof TimeoutException).count();
            long unknown = failures.size() - maxCreateReached - timeout;
            for (CommandRegistrationResult failure : failures) {
                LOGGER.debug("Registration failure in {}", failure.getGuild().getName());
                failure.getException().printStackTrace();
            }
            LOGGER.error("Failed to register slash commands in {}/{} guilds, max create reached = {}, time out = {}, unknown = {}",
                    failures.size(), guilds.size(), maxCreateReached, timeout, unknown);
        }, f -> {
            if (f instanceof TimeoutException) {
                LOGGER.error("Registration of commands timed out for {} guilds", guilds.size());
            } else f.printStackTrace();
        });
    }
}
