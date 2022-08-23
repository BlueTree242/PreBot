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
}
