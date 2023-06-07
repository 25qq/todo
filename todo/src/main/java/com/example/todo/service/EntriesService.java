package com.example.todo.service;

import com.example.todo.data.entity.Entry;
import com.example.todo.data.info.EntryInfo;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public interface EntriesService {
    Entry createEntry(@NotNull Long userId, @NotNull EntryInfo entryInfo);

    Entry updateEntry(@NotNull Long entryId, @NotNull EntryInfo entryInfo);

    void openEntries(@NotNull Long projectId, @NotNull LocalDate from, @NotNull LocalDate to);

    void closeEntries(@NotNull Long projectId, @NotNull LocalDate from, @NotNull LocalDate to);

    void deleteEntry(@NotNull Long entryId);
}
