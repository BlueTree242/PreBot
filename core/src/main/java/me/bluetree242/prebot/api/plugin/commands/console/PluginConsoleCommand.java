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

package me.bluetree242.prebot.api.plugin.commands.console;

import lombok.Getter;
import me.bluetree242.prebot.api.commands.console.ConsoleCommand;
import me.bluetree242.prebot.api.plugin.Plugin;

public abstract class PluginConsoleCommand implements ConsoleCommand {
    @Getter
    private final String name;
    @Getter
    private final String[] aliases;
    @Getter
    private final String description;
    private final Plugin plugin;

    public PluginConsoleCommand(Plugin plugin, String name, String description, String... aliases) {
        this.plugin = null;
        if (plugin == null) throw new IllegalArgumentException("Plugin may not be null.");
        this.name = name;
        this.aliases = aliases;
        this.description = description;
    }

    public final Plugin getPlugin() {
        return plugin;
    }
}
