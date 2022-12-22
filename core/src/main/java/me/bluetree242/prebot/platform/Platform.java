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

package me.bluetree242.prebot.platform;

import lombok.Getter;
import me.bluetree242.prebot.api.PreBot;
import me.bluetree242.prebot.api.commands.console.ConsoleCommand;
import me.bluetree242.prebot.api.commands.console.ConsoleCommandResponder;
import org.slf4j.Logger;

/**
 * A platform that runs prebot
 * @see PlatformType
 */
public abstract class Platform {
    private static Platform platform = null;

    /**
     * get the instance of current platform, would NEVER change
     * @return the instance of current platform
     */
    public static Platform getInstance() {
        return platform;
    }

    protected static void setPlatform(Platform p) {
        if (platform != null) throw new IllegalStateException();
        platform = p;
    }


    public enum PlatformType {
        /**
         * Minecraft Platform, this is used as a parent for Spigot, Bungee and Velocity types.
         */
        MINECRAFT,
        /**
         * The <a href="https://www.spigotmc.org/">SpigotMC</a> Platform
         */
        SPIGOT(MINECRAFT),
        /**
         * The <a href="https://www.spigotmc.org/wiki/bungeecord/">Bungeecord Proxy</a> Platform
         */
        BUNGEE(MINECRAFT),
        /**
         * The <a href="https://velocitypowered.com/">VelocityPowered Proxy</a> Platform
         */
        VELOCITY(MINECRAFT),
        /**
         * The Standalone Platform, where prebot is running alone not as a plugin on any other software, you can consider this "no platform"
         */
        STANDALONE;

        @Getter
        private final PlatformType parent;

        PlatformType(PlatformType parent) {
            this.parent = parent;
        }
        PlatformType() {
            this.parent = null;
        }

        /**
         * Check if this platform equals the provided platform, or the provided platform is parent of this platform
         * @param platform platform to check on
         * @return true if provided platform equals or parent of this platform, false otherwise.
         */
        public boolean is(PlatformType platform) {
            return parent != null && (platform == parent || this == platform);
        }
    }

    public abstract PlatformType getType();

    /**
     * Check if this platform is Minecraft
     * @return true if this is a minecraft-based platform, false otherwise
     */
    public boolean isMinecraft() {
        return getType().is(PlatformType.MINECRAFT);
    }

    /**
     * Check if this platform is Standalone (running jar not as a plugin on another platform)
     * @return true if this is a standalone platform, false otherwise
     */
    public boolean isStandalone() {
        return getType() == PlatformType.STANDALONE;
    }

    /**
     * Get the logger used to log in this platform according to class name
     * @return the class name to get a logger for
     */
    public abstract Logger getLogger(Class<?> clz);

    public abstract PreBot getPreBot();

    public ConsoleCommandResponder getConsoleCommandResponder(ConsoleCommand cmd) {
        return new ConsoleCommandResponder(cmd);
    }

    public void onStop() {}
}