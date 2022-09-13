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

package me.bluetree242.prebot.api.plugin.commands.discord.context;

import me.bluetree242.prebot.api.commands.discord.context.UserContextCommand;
import me.bluetree242.prebot.api.plugin.Plugin;
import me.bluetree242.prebot.api.plugin.commands.discord.PluginDiscordCommand;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public abstract class PluginUserContextCommand extends PluginDiscordCommand implements UserContextCommand {
    public PluginUserContextCommand(Plugin plugin, CommandData data, boolean admin) {
        super(plugin, data, admin);
        if (!(data.getType() == Command.Type.USER))
            throw new IllegalArgumentException("Command Data must be of type USER");
    }
}
