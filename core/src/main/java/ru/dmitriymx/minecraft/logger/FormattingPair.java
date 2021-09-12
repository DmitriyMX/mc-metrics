package ru.dmitriymx.minecraft.logger;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class FormattingPair {

    private final String message;
    private final Throwable throwable;

    public FormattingPair(String message) {
        this.message = message;
        this.throwable = null;
    }
}
