package me.bluetree242.prebot.api.exceptions.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InvalidTypeException extends RuntimeException{
    @Getter
    private final String config;

    @Override
    public String getMessage() {
        return config + " has invalid types! Please make sure all types in your config are correct.";
    }
}
