package me.bluetree242.prebot.core.plugin.logging;

import lombok.Getter;
import me.bluetree242.prebot.plugin.PluginDescription;
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
