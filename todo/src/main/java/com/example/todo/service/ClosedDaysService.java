package com.example.todo.service;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public interface ClosedDaysService {
    void openDays(@NotNull Long projectId, @NotNull LocalDate from, @NotNull LocalDate to);

    void closeDays(@NotNull Long projectId, @NotNull LocalDate from, @NotNull LocalDate to);
}
