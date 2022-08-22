package me.bluetree242.prebot;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;

public abstract class LoggerProvider {
    @Getter
    @Setter
    private static LoggerProvider provider;

    public abstract Logger getLogger(Class<?> clz);
}
