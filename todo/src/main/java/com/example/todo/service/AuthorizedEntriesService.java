package com.example.todo.service;


import com.example.todo.data.entity.Entry;
import com.example.todo.data.entity.user.User;
import com.example.todo.data.info.EntryInfo;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public interface AuthorizedEntriesService {
    Entry createEntry(@NotNull User user, @NotNull Long userId, @NotNull EntryInfo entryInfo);

    Entry updateEntry(@NotNull User user, @NotNull Long entryId, @NotNull EntryInfo entryInfo);

    void openEntries(@NotNull User user, @NotNull Long projectId, @NotNull LocalDate from, @NotNull LocalDate to);

    void closeEntries(@NotNull User user, @NotNull Long projectId, @NotNull LocalDate from, @NotNull LocalDate to);

    void deleteEntry(@NotNull User user, @NotNull Long entryId);
}
