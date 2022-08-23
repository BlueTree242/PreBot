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

package me.bluetree242.prebot.core.plugin.logging;

import lombok.Getter;
import me.bluetree242.prebot.api.plugin.PluginDescription;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.slf4j.helpers.LegacyAbstractLogger;
import org.slf4j.spi.LoggingEventBuilder;

public class JarPluginLogger extends LegacyAbstractLogger {
    @Getter
    private final Logger parent;
    @Getter
    private final PluginDescription plugin;

    public JarPluginLogger(Logger parent, PluginDescription plugin) {
        super();
        this.parent = parent;
        this.plugin = plugin;
    }

    @Override
    protected String getFullyQualifiedCallerName() {
        return parent.getClass().getName();
    }

    @Override
    protected void handleNormalizedLoggingCall(Level level, Marker marker, String msg, Object[] arguments, Throwable throwable) {
        LoggingEventBuilder builder = parent.atLevel(level).setCause(throwable);
        if (marker != null) builder = builder.addMarker(marker);
        if (arguments != null)
            for (Object argument : arguments) {
                builder = builder.addArgument(argument);
            }
        builder.log("[" + plugin.getName() + "]" + " " + msg);
    }

    @Override
    public boolean isTraceEnabled() {
        return parent.isTraceEnabled();
    }

    @Override
    public boolean isDebugEnabled() {
        return parent.isDebugEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return parent.isInfoEnabled();
    }

    @Override
    public boolean isWarnEnabled() {
        return parent.isWarnEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return parent.isErrorEnabled();
    }
}
