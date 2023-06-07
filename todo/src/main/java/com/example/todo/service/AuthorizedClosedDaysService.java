package com.example.todo.service;

import com.example.todo.data.entity.user.User;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public interface AuthorizedClosedDaysService {
    void openDays(@NotNull User user, @NotNull Long projectId, @NotNull LocalDate from, @NotNull LocalDate to);

    void closeDays(@NotNull User user, @NotNull Long projectId, @NotNull LocalDate from, @NotNull LocalDate to);
}
