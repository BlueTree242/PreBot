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

package me.bluetree242.prebot.api.commands.console;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.bluetree242.prebot.api.LoggerProvider;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

/**
 * This class is responsible for responding to console commands
 */
@RequiredArgsConstructor
public class ConsoleCommandResponder {
    private static final Logger LOGGER = LoggerProvider.getProvider().getLogger(ConsoleCommandResponder.class);
    @Getter(onMethod_={@Nullable})
    private final ConsoleCommand command;

    /**
     * Sends a response to the command
     * @param toSend Text to respond with
     */
    public void send(String toSend) {
        LOGGER.info(toSend);
    }
}
