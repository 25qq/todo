package com.example.todo.service;


import com.example.todo.data.entity.Action;
import com.example.todo.data.entity.user.User;
import com.example.todo.data.info.ActionInfo;
import jakarta.validation.constraints.NotNull;


public interface AuthorizedProjectActionsService {
    Action createAction(@NotNull User user, @NotNull Long projectId, @NotNull ActionInfo actionInfo);

    Action updateAction(@NotNull User user, @NotNull Long actionId, @NotNull ActionInfo actionInfo);

    void activateAction(@NotNull User user, @NotNull Long actionId);

    boolean deleteOrDeactivateAction(@NotNull User user, @NotNull Long actionId);
}
