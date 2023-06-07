package com.example.todo.common.translator;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MessageSourceBasedTranslator implements Translator {

    private final MessageSource messages;

    public String toLocale(@NonNull String key, Locale locale) {
        return messages.getMessage(key, null, locale);
    }

}
