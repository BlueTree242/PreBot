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

package me.bluetree242.prebot.core.discordcommands;

import lombok.Getter;
import me.bluetree242.prebot.api.LoggerProvider;
import me.bluetree242.prebot.api.PreBot;
import me.bluetree242.prebot.api.PreBotVersion;
import me.bluetree242.prebot.api.color.TextColor;
import me.bluetree242.prebot.api.commands.discord.slash.SlashCommand;
import me.bluetree242.prebot.api.plugin.Plugin;
import me.bluetree242.prebot.core.utils.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class PreBotDiscordCommand implements SlashCommand {
    private static final Logger LOGGER = LoggerProvider.getProvider().getLogger(PreBotDiscordCommand.class);
    private final PreBot core;
    @Getter
    private final SlashCommandData data;

    public PreBotDiscordCommand(PreBot core) {
        data = Commands.slash("prebot", "Commands for prebot management").setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_SERVER))
                .addSubcommands(new SubcommandData("plugins", "Get list of installed plugins"))
                .addSubcommands(new SubcommandData("info", "Get more details of current prebot instance"))
                .addSubcommands(new SubcommandData("reload", "Reload PreBot or a plugin").addOption(OptionType.STRING, "plugin", "Plugin to reload, or all", false, true));
        this.core = core;
    }


    @Override
    public void onCommand(SlashCommandInteractionEvent e) {
        if (e.getSubcommandName() == null) return; //impossible, unless some plugin is messing with the system
        if (e.getSubcommandName().equals("plugins")) {
            StringJoiner joiner = new StringJoiner(", ", "", "");
            for (Plugin plugin : core.getPluginManager().getPlugins()) {
                joiner.add((plugin.isEnabled() ? TextColor.GREEN.getAnsiCode() : TextColor.RED.getAnsiCode()) + plugin.getDescription().getName() + TextColor.RESET.getAnsiCode());
            }
            e.reply("Running PreBot v" + PreBotVersion.VERSION + "\n```ansi\n" + TextColor.WHITE.getAnsiCode() + "Plugins (" + core.getPluginManager().getPlugins().size() + "): " + joiner + "\n```").setEphemeral(true).queue();
        } else if (e.getSubcommandName().equals("info")) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.ORANGE)
                    .setTitle("PreBot Main Information")
                    .setThumbnail(e.getJDA().getSelfUser().getEffectiveAvatarUrl());
            embed.addField("Version", PreBotVersion.VERSION, true)
                    .addField("Commit Hash", PreBotVersion.COMMIT_HASH, true)
                    .addField("Branch", PreBotVersion.BRANCH, true)
                    .addField("Current Shard", e.getJDA().getShardInfo().getShardId() + "", true)
                    .addField("Total Shards", e.getJDA().getShardInfo().getShardTotal() + "", true)
                    .addField("Enabled Plugins", core.getPluginManager().getPlugins().stream().filter(Plugin::isEnabled).count() + "", true);
            e.replyEmbeds(embed.build()).setEphemeral(true).queue();
        }else if (e.getSubcommandName().equals("reload")) {
            OptionMapping pluginOption = e.getOption("plugin");
            if (pluginOption == null) {
                try {
                    core.reload();
                    e.reply(":white_check_mark: Successfully reloaded PreBot").setEphemeral(true).queue();
                } catch (Exception ex) {
                    LOGGER.error("Failed to reload PreBot (reload requested from discord)", ex);
                    e.reply(":x: Failed to reload PreBot, Console might contain more details\n" + codeBlockError(ex)).setEphemeral(true).queue();
                }
            } else {
                String name = pluginOption.getAsString();
                if (name.equalsIgnoreCase("all")) {
                    Set<String> failed = new HashSet<>();
                    for (Plugin plugin : core.getPluginManager().getPlugins()) {
                        try {
                            if (plugin.isEnabled()) plugin.reload();
                        } catch (Exception ex) {
                            LOGGER.error("Failed to reload plugin " + plugin.getDescription().getName() + " (reload requested from discord)", ex);
                            failed.add(plugin.getDescription().getName());
                        }
                    }
                    if (failed.isEmpty()) {
                        e.reply(":white_check_mark: Successfully reloaded all plugins").setEphemeral(true).queue();
                    } else {
                        e.reply("Reloaded all plugins but " + failed.size() + " plugins failed: " + String.join(", ", failed) + ". Check console for more details.").setEphemeral(true).queue();
                    }
                } else {
                    Plugin plugin = core.getPluginManager().getPluginByName(name);
                    if (plugin == null) {
                        e.reply(":x: Plugin not found").setEphemeral(true).queue();
                    } else {
                        if (!plugin.isEnabled()) {
                            e.reply(":x: Plugin is disabled").setEphemeral(true).queue();
                        }
                        try {
                            plugin.reload();
                            e.reply(":white_check_mark: Successfully reloaded " + plugin.getDescription().getName()).setEphemeral(true).queue();
                        } catch (Exception ex) {
                            LOGGER.error("Failed to reload " + plugin.getDescription().getName() + " (reload requested from discord)", ex);
                            e.reply(":x: Failed to reload plugin, Console might contain more details\n" + codeBlockError(ex)).setEphemeral(true).queue();
                        }
                    }
                }
            }
        }
    }

    private String codeBlockError(Throwable er) {
        return "```java\n" + Utils.trim(ExceptionUtils.getStackTrace(er), 2900) + "\n```";
    }

    @Override
    public void onAutoComplete(CommandAutoCompleteInteractionEvent e) {
        if (!e.getFocusedOption().getName().equals("plugin")) return;
        List<String> result = core.getPluginManager().getPlugins().stream().filter(Plugin::isEnabled).map(p -> p.getDescription().getName()).filter(n -> n.toLowerCase(Locale.ROOT).startsWith(e.getFocusedOption().getValue())).collect(Collectors.toList());
        result.add("all");
        e.replyChoiceStrings(result).queue();
    }

    @Override
    public boolean isAdmin() {
        return true;
    }
}
