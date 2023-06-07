package com.example.todo.common.errors;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class IncorrectArgumentException extends TimeTrackerException {

    public IncorrectArgumentException(String message) {
        super(message);
    }

    public IncorrectArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectArgumentException(Throwable cause) {
        super(cause);
    }

    public IncorrectArgumentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
