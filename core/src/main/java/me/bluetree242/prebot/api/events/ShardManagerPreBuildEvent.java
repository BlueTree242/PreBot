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
import me.bluetree242.prebot.api.PreBot;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;

/**
 * This event is right before we are about to build our {@link net.dv8tion.jda.api.sharding.ShardManager}.
 * You always receive that after we set up our listeners, plugin listeners activity, gateway intents and status.<br>
 * {@link PreBot#getIntents()} and {@link PreBot#getCacheFlags()} are still modifiable the time this event is fired.
 */
@CustomEvent
@RequiredArgsConstructor
public class ShardManagerPreBuildEvent {
    @Getter
    private final DefaultShardManagerBuilder builder;
}
