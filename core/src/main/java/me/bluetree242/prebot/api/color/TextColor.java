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

package me.bluetree242.prebot.api.color;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This class is for logging colors, using minecraft's color codes.<br>
 * {@link TextColor#toString()} returns a string when used in logging it will make the rest of the message colored<br>
 * <strong>This does not use ANSI, it uses Minecraft Color Codes (prefixed with §) and log4j or the library we use <a href="https://github.com/Minecrell/TerminalConsoleAppender">TerminalConsoleAppender</a> should convert them to colors</strong>
 */
@RequiredArgsConstructor
public enum TextColor {
    DARK_RED("4", "\u001B[0;31m"),
    RED("c", "\u001B[0;31;1m"),
    GOLD("6", "\u001B[0;33m"),
    YELLOW("e", "\u001b[33m"),
    DARK_GREEN("2", "\u001B[0;32m"),
    GREEN("a", "\u001b[32m"),
    AQUA("b", "\u001B[0;36;1m"),
    DARK_AQUA("3", "\u001B[0;36m"),
    DARK_BLUE("1", "\u001B[0;34m"),
    BLUE("9", "\u001B[0;34;1m"),
    LIGHT_PURPLE("d", "\u001B[0;35;1m"),
    WHITE("f", "\u001B[0;37;1m"),
    GRAY("7", "\u001B[0;37m"),
    DARK_GRAY("8", "\u001B[0;30;1m"),
    BLACK("0", "\u001B[0;30m"),
    RESET("r", "\u001B[0m");
    @Getter
    public static final String codePrefix = "§";
    @Getter
    private final String code;
    @Getter
    private final String ansiCode;

    /**
     * Strips any color codes from a message
     *
     * @param s string to strip from
     * @return provided string but without any color codes
     */
    public static String strip(String s) {
        String result = s;
        for (TextColor value : values()) {
            if (s.contains(codePrefix + value.code)) result = result.replace(codePrefix + value.code, "");
        }
        return result;
    }

    @Override
    public String toString() {
        return codePrefix + code;
    }
}
