package com.example.todo.service;


import com.example.todo.data.entity.Action;
import com.example.todo.data.info.ActionInfo;
import jakarta.validation.constraints.NotNull;

public interface ProjectActionsService {
    Action createAction(@NotNull Long projectId, @NotNull ActionInfo actionInfo);

    Action updateAction(@NotNull Long actionId, @NotNull ActionInfo actionInfo);

    void activateAction(@NotNull Long actionId);

    boolean deleteOrDeactivateAction(@NotNull Long actionId);

}
