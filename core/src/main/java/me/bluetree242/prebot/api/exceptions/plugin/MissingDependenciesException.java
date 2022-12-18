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

package me.bluetree242.prebot.api.exceptions.plugin;

import me.bluetree242.prebot.api.LoggerProvider;
import me.bluetree242.prebot.api.plugin.PluginDescription;
import org.slf4j.Logger;

import java.util.Set;

/**
 * Thrown when trying to load a plugin and it has missing dependencies
 */
public class MissingDependenciesException extends InvalidPluginException {
    private final Logger LOGGER = LoggerProvider.getLogger(MissingDependenciesException.class);

    public MissingDependenciesException(PluginDescription description, Set<String> missingDependencies) {
        super(description.getName() + " requires " + missingDependencies.size() + " dependency(s). Please install (" + String.join(", ", missingDependencies) + ")");
    }

    public void log() {
        LOGGER.error(getMessage());
    }
}
