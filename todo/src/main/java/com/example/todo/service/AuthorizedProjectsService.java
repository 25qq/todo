package com.example.todo.service;


import com.example.todo.data.entity.Project;
import com.example.todo.data.entity.user.User;
import com.example.todo.data.info.ProjectInfo;
import jakarta.validation.constraints.NotNull;

public interface AuthorizedProjectsService {
    Project createProject(@NotNull User user, @NotNull ProjectInfo project);

    Project updateProject(@NotNull User user, @NotNull Long projectId, @NotNull ProjectInfo project);

    void activateProject(@NotNull User user, @NotNull Long projectId);

    boolean deleteOrDeactivateProject(@NotNull User user, @NotNull Long projectId);
}
