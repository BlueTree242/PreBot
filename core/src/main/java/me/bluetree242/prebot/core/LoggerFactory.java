package me.bluetree242.prebot.core;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;

public abstract class LoggerFactory {
    @Getter
    @Setter
    private static LoggerFactory factory;

    public abstract Logger getLogger(Class<?> clz);
}
