package com.example.todo.service;


import com.example.todo.data.entity.Project;
import com.example.todo.data.info.ProjectInfo;
import jakarta.validation.constraints.NotNull;

public interface ProjectService {
    Project createProject(@NotNull ProjectInfo projectInfo);

    Project updateProject(@NotNull Long projectId, @NotNull ProjectInfo project);

    void activateProject(@NotNull Long projectId);

    boolean deleteOrDeactivateProject(@NotNull Long projectId);
}
