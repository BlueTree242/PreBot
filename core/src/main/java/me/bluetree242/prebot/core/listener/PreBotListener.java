package me.bluetree242.prebot.core.listener;

import lombok.RequiredArgsConstructor;
import me.bluetree242.jdaeventer.DiscordListener;
import me.bluetree242.jdaeventer.annotations.HandleEvent;
import me.bluetree242.prebot.api.plugin.Plugin;
import me.bluetree242.prebot.core.PreBotMain;
import net.dv8tion.jda.api.events.ReadyEvent;

import java.util.HashSet;

@RequiredArgsConstructor
public class PreBotListener implements DiscordListener {
    private final PreBotMain core;
    @HandleEvent
    public void onReady(ReadyEvent e) {
        for (Plugin plugin : new HashSet<>(core.getPluginManager().getPlugins())) {
            plugin.onShardOnline(e.getJDA());
        }
    }
}
