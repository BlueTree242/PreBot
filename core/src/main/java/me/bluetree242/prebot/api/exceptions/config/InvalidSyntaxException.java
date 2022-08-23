package me.bluetree242.prebot.api.exceptions.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InvalidSyntaxException extends RuntimeException{
    @Getter
    private final String config;

    @Override
    public String getMessage() {
        return config + " has invalid syntax! Check the syntax with https://yaml-online-parser.appspot.com/";
    }
}
