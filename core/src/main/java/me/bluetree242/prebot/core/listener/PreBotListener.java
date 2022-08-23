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
import me.bluetree242.prebot.api.plugin.Plugin;
import me.bluetree242.prebot.core.PreBotMain;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ReconnectedEvent;

import java.util.HashSet;

@RequiredArgsConstructor
public class PreBotListener implements DiscordListener {
    private final PreBotMain core;
    @HandleEvent
    public void onReady(ReadyEvent e) {
        for (Plugin plugin : new HashSet<>(core.getPluginManager().getPlugins())) {
            plugin.onShardReady(e.getJDA());
        }
    }

    @HandleEvent
    public void onReconnect(ReconnectedEvent e) {
        for (Plugin plugin : new HashSet<>(core.getPluginManager().getPlugins())) {
            plugin.onShardReconnect(e.getJDA());
        }
    }
}
