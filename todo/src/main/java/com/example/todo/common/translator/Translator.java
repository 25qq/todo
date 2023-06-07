package com.example.todo.common.translator;

import lombok.NonNull;

import java.util.Locale;

public interface Translator {
    String toLocale(@NonNull String key, Locale locale);
}
