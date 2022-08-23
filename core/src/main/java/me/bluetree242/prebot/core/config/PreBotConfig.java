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

package me.bluetree242.prebot.core.config;

import net.dv8tion.jda.api.OnlineStatus;
import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.ConfHeader;
import space.arim.dazzleconf.annote.ConfKey;
import space.arim.dazzleconf.sorter.AnnotationBasedSorter;

@ConfHeader("PreBot Configuration, Here everything is controlled.\n\n")
public interface PreBotConfig {

    @AnnotationBasedSorter.Order(10)
    @ConfDefault.DefaultString("unset")
    @ConfComments("The bot token to login to discord. This must be valid")
    String token();

    @AnnotationBasedSorter.Order(20)
    @ConfKey("online-status")
    @ConfDefault.DefaultString("ONLINE")
    @ConfComments("The online status of the bot. Possible values: ONLINE, DO_NOT_DISTURB, IDLE, INVISIBLE")
    OnlineStatus online_status();

    @AnnotationBasedSorter.Order(20)
    @ConfKey("activity-status")
    @ConfDefault.DefaultString("playing with PreBot")
    @ConfComments("The Activity Status of the bot.")
    String activity_status();

    @AnnotationBasedSorter.Order(30)
    @ConfKey("executor-size")
    @ConfDefault.DefaultInteger(5)
    @ConfComments("The Number of threads in the executor thread pool. You need to increase this if the bot has more tasks to do at the same time (or many things happen so fast) for example public bots.")
    int executor_size();
}
