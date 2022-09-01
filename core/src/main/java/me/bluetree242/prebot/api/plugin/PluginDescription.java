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

package me.bluetree242.prebot.api.plugin;

import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents Plugin Information
 */
public interface PluginDescription extends Comparable<PluginDescription> {

    /**
     * The Version of the plugin
     *
     * @return the version of the plugin
     */
    @NotNull
    String getVersion();

    /**
     * The authors of the plugin
     *
     * @return Unmodifiable list of authors, cannot be less than 1
     */
    @NotNull
    List<String> getAuthors();

    /**
     * The name of the plugin
     *
     * @return The name of the plugin.
     */
    @NotNull
    String getName();

    /**
     * The path to the main class of the plugin
     *
     * @return the main class path
     */
    @NotNull
    String getMain();

    /**
     * the dependencies of the plugin
     *
     * @return The list of dependencies of the plugin, never null
     */
    @NotNull
    List<String> getDependencies();

    /**
     * the soft dependencies of the plugin, soft dependencies are optional dependencies
     *
     * @return The list of soft dependencies of the plugin, never null
     */
    @NotNull
    List<String> getSoftDependencies();

    /**
     * The Gateway intents this plugin requires.
     *
     * @return The Gateway intents required for this plugin.
     */
    @NotNull
    GatewayIntent[] getRequiredIntents();

    /**
     * The Cache flags this plugin requires.
     *
     * @return The Cache flags required for this plugin.
     */
    @NotNull
    CacheFlag[] getRequiredCacheFlags();
}
