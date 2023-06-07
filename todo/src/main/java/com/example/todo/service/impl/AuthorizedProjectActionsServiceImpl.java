package com.example.todo.service.impl;

import com.example.todo.common.errors.NoPermissionException;
import com.example.todo.data.entity.Action;
import com.example.todo.data.entity.Project;
import com.example.todo.data.entity.user.User;
import com.example.todo.data.enums.ProjectPermissionType;
import com.example.todo.data.info.ActionInfo;
import com.example.todo.data.repo.ActionsRepository;
import com.example.todo.data.repo.ProjectsRepository;
import com.example.todo.data.utils.RolePermissionsHelper;
import com.example.todo.service.AuthorizedProjectActionsService;
import com.example.todo.service.ProjectActionsService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AuthorizedProjectActionsServiceImpl implements AuthorizedProjectActionsService {
    private final RolePermissionsHelper rolePermissionsHelper;
    private final ActionsRepository actionsRepository;
    private final ProjectActionsService projectActionsService;
    private final ProjectsRepository projectsRepository;


    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public Action createAction(@NotNull User user, @NotNull Long projectId, @NotNull ActionInfo actionInfo) {
        Project project = projectsRepository.getOne(projectId);
        if (!rolePermissionsHelper.hasProjectPermission(user, project, ProjectPermissionType.EDIT_PROJECT_ACTIONS)) {
            throw new NoPermissionException("You have no permissions to create this project");
        }

        return projectActionsService.createAction(projectId, actionInfo);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public Action updateAction(@NotNull User user, @NotNull Long actionId, @NotNull ActionInfo actionInfo) {
        Action action = actionsRepository.findWithProjectById(actionId);
        if (!rolePermissionsHelper.hasProjectPermission(user, action.getProject(), ProjectPermissionType.EDIT_PROJECT_ACTIONS)) {
            throw new NoPermissionException("You have no permissions to update this project");
        }

        return projectActionsService.updateAction(actionId, actionInfo);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public void activateAction(@NotNull User user, Long actionId) {
        Action action = actionsRepository.findWithProjectById(actionId);

        if (!rolePermissionsHelper.hasProjectPermission(user, action.getProject(), ProjectPermissionType.EDIT_PROJECT_ACTIONS)) {
            throw new NoPermissionException("You have no permissions to update this project");
        }

        projectActionsService.activateAction(actionId);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public boolean deleteOrDeactivateAction(@NotNull User user, Long actionId) {
        Action action = actionsRepository.findWithProjectById(actionId);

        if (!rolePermissionsHelper.hasProjectPermission(user, action.getProject(), ProjectPermissionType.EDIT_PROJECT_ACTIONS)) {
            throw new NoPermissionException("You have no permissions to update this project");
        }

        return projectActionsService.deleteOrDeactivateAction(actionId);
    }


}
