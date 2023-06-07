package com.example.todo.common.errors;


import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TimeTrackerException extends RuntimeException {

    public TimeTrackerException(String message) {
        super(message);
    }

    public TimeTrackerException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimeTrackerException(Throwable cause) {
        super(cause);
    }

    public TimeTrackerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


}
