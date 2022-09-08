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
import me.bluetree242.prebot.api.PreBot;
import me.bluetree242.prebot.api.PreBotVersion;
import me.bluetree242.prebot.api.color.TextColor;
import me.bluetree242.prebot.api.commands.discord.SlashCommand;
import me.bluetree242.prebot.api.plugin.Plugin;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.awt.*;
import java.util.StringJoiner;

public class PreBotDiscordCommand implements SlashCommand {
    private final PreBot core;
    @Getter
    private final SlashCommandData data;

    public PreBotDiscordCommand(PreBot core) {
        data = Commands.slash("prebot", "Commands for prebot management").setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_SERVER))
                .addSubcommands(new SubcommandData("plugins", "Get list of installed plugins"))
                .addSubcommands(new SubcommandData("info", "Get more details of current prebot instance"));
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
                    .addField("Enabled Plugins", core.getPluginManager().getPlugins().stream().filter(Plugin::isEnabled).count() + "", false);
            e.replyEmbeds(embed.build()).setEphemeral(true).queue();
        }
    }

    @Override
    public boolean isAdmin() {
        return true;
    }
}
