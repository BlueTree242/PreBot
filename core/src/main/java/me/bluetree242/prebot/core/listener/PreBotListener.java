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

package me.bluetree242.prebot.core.listener;

import lombok.RequiredArgsConstructor;
import me.bluetree242.jdaeventer.DiscordListener;
import me.bluetree242.jdaeventer.HandlerPriority;
import me.bluetree242.jdaeventer.annotations.HandleEvent;
import me.bluetree242.jdaeventer.objects.EventInformation;
import me.bluetree242.prebot.api.LoggerProvider;
import me.bluetree242.prebot.api.commands.discord.DiscordCommand;
import me.bluetree242.prebot.api.commands.discord.slash.SlashCommand;
import me.bluetree242.prebot.api.plugin.Plugin;
import me.bluetree242.prebot.api.plugin.commands.discord.PluginDiscordCommand;
import me.bluetree242.prebot.core.PreBotMain;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.session.SessionRecreateEvent;
import net.dv8tion.jda.api.events.session.ShutdownEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.requests.CloseCode;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.HashSet;

@RequiredArgsConstructor
public class PreBotListener implements DiscordListener {
    private static final Logger LOGGER = LoggerProvider.getLogger(PreBotListener.class);
    private final PreBotMain core;

    @HandleEvent
    public void onReady(ReadyEvent e) {
        for (Plugin plugin : new HashSet<>(core.getPluginManager().getPlugins())) {
            try {
                if (plugin.isEnabled()) plugin.onShardReady(e.getJDA());
            } catch (Exception ex) {
                LOGGER.error("An error occurred while passing onShardReady to " + plugin.getDescription().getName(), ex);
            }
        }
        core.getDiscordCommandManager().registerCommands(e.getJDA().getGuilds());
    }

    @HandleEvent
    public void onReconnect(SessionRecreateEvent e) {
        for (Plugin plugin : new HashSet<>(core.getPluginManager().getPlugins())) {
            try {
                if (plugin.isEnabled()) plugin.onShardReconnect(e.getJDA());
            } catch (Exception ex) {
                LOGGER.error("An error occurred while passing onShardReconnect to " + plugin.getDescription().getName(), ex);
            }
        }
        core.getDiscordCommandManager().registerCommands(e.getJDA().getGuilds());
    }

    @HandleEvent
    public void onJoinServer(GuildJoinEvent e) {
        core.getDiscordCommandManager().registerCommands(Collections.singletonList(e.getGuild()));
    }

    @HandleEvent
    public void onShutdown(ShutdownEvent e) {
        if (core.isStopped()) return;
        if (core.getShardManager() != null) {
            long jdaRunning = core.getShardManager().getShardCache().stream().filter(s -> s.getStatus() != JDA.Status.SHUTDOWN && s.getStatus() != JDA.Status.SHUTTING_DOWN && s.getStatus() != JDA.Status.FAILED_TO_LOGIN).count();
            if (jdaRunning == 0) {
                if (e.getCloseCode() == CloseCode.DISALLOWED_INTENTS) {
                    String[] intents = core.getIntents().stream().filter(i -> i == GatewayIntent.GUILD_PRESENCES || i == GatewayIntent.MESSAGE_CONTENT || i == GatewayIntent.GUILD_MEMBERS)
                            .map(this::getFriendlyName).toArray(String[]::new);
                    LOGGER.error("Your bot is not allowed to request one of these intents, please fix in your developer portal. ({})", String.join(", ", intents));
                } else LOGGER.info("No Shards are running, or queued. Shutting down..");
                core.stop();
            }
        }
    }

    private String getFriendlyName(GatewayIntent intent) {
        switch (intent) {
            case GUILD_PRESENCES:
                return "Presence Intent";
            case MESSAGE_CONTENT:
                return "Message Content Intent";
            case GUILD_MEMBERS:
                return "Server Member Intent";
        }
        return intent.name();
    }

    @HandleEvent(priority = HandlerPriority.LOWEST)
    public void onPreCommand(GenericCommandInteractionEvent e, EventInformation info) {
        final DiscordCommand command;
        if (e.getCommandType() == Command.Type.SLASH)
            command = core.getDiscordCommandManager().getSlashCommands().get(e.getName());
        else if (e.getCommandType() == Command.Type.USER)
            command = core.getDiscordCommandManager().getUserCommands().get(e.getName());
        else if (e.getCommandType() == Command.Type.MESSAGE)
            command = core.getDiscordCommandManager().getMessageCommands().get(e.getName());
        else command = null;
        if (command != null) info.addNote("command", command);
    }

    @HandleEvent(priority = HandlerPriority.MONITOR, ignoreCancelMark = true)
    public void onCommand(GenericCommandInteractionEvent e, EventInformation info) {
        DiscordCommand command = (DiscordCommand) info.getNote("command");
        if (command == null) return;
        if (command instanceof PluginDiscordCommand) {
            Plugin plugin = ((PluginDiscordCommand) command).getPlugin();
            if (!plugin.isEnabled()) {
                if (!core.isAdmin(e.getUser())) {
                    e.reply(":x: This command cannot be executed right now. Please contact bot admins").setEphemeral(true).queue();
                } else {
                    e.reply(":x: This command cannot be executed because plugin " + plugin.getDescription().getName() + " v" + plugin.getDescription().getVersion() + " is not enabled.").setEphemeral(true).queue();
                }
                return;
            }
        }
        if (command.isAdmin() && !core.isAdmin(e.getUser())) {
            e.reply(":x: This command can only be used by bot admins").setEphemeral(true).queue();
        } else {
            try {
                command.onCommand(e, info);
            } catch (Exception x) {
                if (!e.isAcknowledged()) {
                    if (!core.isAdmin(e.getUser()))
                        e.reply(":x: An error occurred. Please contact bot admins").setEphemeral(true).queue();
                    else
                        e.reply(":x: An error occurred. Please check console for more details").setEphemeral(true).queue();
                }
                LOGGER.error("An error occurred while executing command /" + command.getData().getName(), x);
            }
        }
    }

    @HandleEvent(priority = HandlerPriority.LOWEST)
    public void onPreAutoComplete(CommandAutoCompleteInteractionEvent e, EventInformation info) {
        SlashCommand command = core.getDiscordCommandManager().getSlashCommands().get(e.getName());
        if (command != null) info.addNote("command", command);
    }

    @HandleEvent(priority = HandlerPriority.MONITOR, ignoreCancelMark = true)
    public void onAutoComplete(CommandAutoCompleteInteractionEvent e, EventInformation info) {
        SlashCommand command = (SlashCommand) info.getNote("command");
        if (command == null) return;
        if (command.isAdmin() && !core.isAdmin(e.getUser())) return;
        if (command instanceof PluginDiscordCommand) {
            Plugin plugin = ((PluginDiscordCommand) command).getPlugin();
            if (!plugin.isEnabled()) return;
        }
        try {
            command.onAutoComplete(e, info);
        } catch (Exception x) {
            LOGGER.error("An error occurred while auto completing command /" + command.getData().getName(), x);
        }
    }
}
