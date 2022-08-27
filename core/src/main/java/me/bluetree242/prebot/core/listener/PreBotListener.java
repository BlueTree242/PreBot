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
import me.bluetree242.jdaeventer.annotations.HandleEvent;
import me.bluetree242.prebot.api.LoggerProvider;
import me.bluetree242.prebot.api.plugin.Plugin;
import me.bluetree242.prebot.core.PreBotMain;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ReconnectedEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.requests.CloseCode;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;

import java.util.HashSet;

@RequiredArgsConstructor
public class PreBotListener implements DiscordListener {
    private static final Logger LOGGER = LoggerProvider.getProvider().getLogger(PreBotListener.class);
    private final PreBotMain core;

    @HandleEvent
    public void onReady(ReadyEvent e) {
        for (Plugin plugin : new HashSet<>(core.getPluginManager().getPlugins())) {
            if (plugin.isEnabled()) plugin.onShardReady(e.getJDA());
        }
    }

    @HandleEvent
    public void onReconnect(ReconnectedEvent e) {
        for (Plugin plugin : new HashSet<>(core.getPluginManager().getPlugins())) {
            if (plugin.isEnabled()) plugin.onShardReconnect(e.getJDA());
        }
    }

    @HandleEvent
    public void onShutdown(ShutdownEvent e) {
        if (core.getShardManager() != null) {
            long jdaRunning = core.getShardManager().getShardCache().stream().filter(s -> s.getStatus() != JDA.Status.SHUTDOWN && s.getStatus() != JDA.Status.SHUTTING_DOWN && s.getStatus() != JDA.Status.FAILED_TO_LOGIN).count();
            if (jdaRunning == 0) {
                if (e.getCloseCode() == CloseCode.DISALLOWED_INTENTS) {
                    String[] intents = core.getIntents().stream().filter(i -> i == GatewayIntent.GUILD_PRESENCES || i == GatewayIntent.MESSAGE_CONTENT || i == GatewayIntent.GUILD_MEMBERS)
                            .map(this::getFriendlyName).toArray(String[]::new);
                    LOGGER.error("Your bot is not allowed to request one of these intents, please fix in your developer portal. ({})", String.join(", ", intents));
                } else LOGGER.info("No Shards are running, or queued. Shutting down..");
                core.getShardManager().shutdown();
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
}
