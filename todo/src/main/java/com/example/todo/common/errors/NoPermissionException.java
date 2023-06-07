package com.example.todo.common.errors;


import lombok.NoArgsConstructor;


@NoArgsConstructor
public class NoPermissionException extends TimeTrackerException {

    public NoPermissionException(String message) {
        super(message);
    }


    public NoPermissionException(String message, Throwable cause) {
        super(message, cause);
    }


    public NoPermissionException(Throwable cause) {
        super(cause);
    }

    public NoPermissionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
