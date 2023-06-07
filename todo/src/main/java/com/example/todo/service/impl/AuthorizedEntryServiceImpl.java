package com.example.todo.service.impl;

import com.example.todo.common.errors.NoPermissionException;
import com.example.todo.data.entity.Action;
import com.example.todo.data.entity.Entry;
import com.example.todo.data.entity.Project;
import com.example.todo.data.entity.user.User;
import com.example.todo.data.enums.ProjectPermissionType;
import com.example.todo.data.info.EntryInfo;
import com.example.todo.data.repo.ActionsRepository;
import com.example.todo.data.repo.EntriesRepository;
import com.example.todo.data.repo.ProjectsRepository;
import com.example.todo.data.utils.RolePermissionsHelper;
import com.example.todo.service.AuthorizedEntriesService;
import com.example.todo.service.EntriesService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthorizedEntryServiceImpl implements AuthorizedEntriesService {
    private final RolePermissionsHelper rolePermissionsHelper;
    private final EntriesService entriesService;
    private final EntriesRepository entriesRepository;
    private final ActionsRepository actionsRepository;
    private final ProjectsRepository projectsRepository;



    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public Entry createEntry(@NotNull User user, @NotNull Long userId, @NotNull EntryInfo entryInfo) {
        if (entryInfo.getActionId() == null) {
            throw new NoPermissionException("You have no permissions to create work items for this project");
        }

        boolean sameUser = user.getId().equals(userId);
        Action action = actionsRepository.getOne(entryInfo.getActionId());
        if (sameUser && !rolePermissionsHelper.hasProjectPermission(user, action.getProject(), ProjectPermissionType.EDIT_MY_LOGS)) {
            throw new NoPermissionException("You have no permissions to create work items for this project");
        } else if (!sameUser) {
            boolean hasProjectPermission = rolePermissionsHelper.hasProjectPermission(user, action.getProject(), ProjectPermissionType.EDIT_PROJECT_LOGS);
            if (!hasProjectPermission) {
                throw new NoPermissionException("You have no permissions to create other people's work items for this project");
            }
        }

        return entriesService.createEntry(userId, entryInfo);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public Entry updateEntry(@NotNull User user, @NotNull Long entryId, @NotNull EntryInfo entryInfo) {
        if (entryInfo.getActionId() == null) {
            throw new NoPermissionException("You have no permissions to create work items for this project");
        }

        Entry entry = entriesRepository.getOne(entryId);
        Project project = entry.getAction().getProject();
        boolean sameUser = user.getId().equals(entry.getUser().getId());
        if (sameUser && !rolePermissionsHelper.hasProjectPermission(user, project, ProjectPermissionType.EDIT_MY_LOGS)) {
            throw new NoPermissionException("You have no permissions to update work items for this project");
        } else if (!sameUser) {
            boolean hasProjectPermission = rolePermissionsHelper.hasProjectPermission(user, project, ProjectPermissionType.EDIT_PROJECT_LOGS);
            if (!hasProjectPermission) {
                throw new NoPermissionException("You have no permissions to update other people's work items for this project");
            }
        }

        return entriesService.updateEntry(entryId, entryInfo);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public void openEntries(@NotNull User user, @NotNull Long projectId, @NotNull LocalDate from, @NotNull LocalDate to) {
        Project project = projectsRepository.getOne(projectId);

        if (!rolePermissionsHelper.hasProjectPermission(user, project, ProjectPermissionType.VIEW_PROJECT_LOGS)) {
            throw new NoPermissionException("You have no permissions to open work items for this project");
        }

        entriesService.openEntries(projectId, from, to);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public void closeEntries(@NotNull User user, @NotNull Long projectId, @NotNull LocalDate from, @NotNull LocalDate to) {
        Project project = projectsRepository.getOne(projectId);

        if (!rolePermissionsHelper.hasProjectPermission(user, project, ProjectPermissionType.VIEW_PROJECT_LOGS)) {
            throw new NoPermissionException("You have no permissions to close work items for this project");
        }

        entriesService.closeEntries(projectId, from, to);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public void deleteEntry(@NotNull User user, @NotNull Long entryId) {
        Entry entry = entriesRepository.findWithUserAndProjectById(entryId);
        Project project = entry.getAction().getProject();

        Objects.requireNonNull(entry);

        boolean sameUser = user.getId().equals(entry.getUser().getId());
        if (sameUser && !rolePermissionsHelper.hasProjectPermission(user, project, ProjectPermissionType.EDIT_MY_LOGS)) {
            throw new NoPermissionException("You have no permissions to delete work items for this project");
        } else if (!sameUser) {
            boolean hasProjectPermission = rolePermissionsHelper.hasProjectPermission(user, project, ProjectPermissionType.EDIT_PROJECT_LOGS);
            if (!hasProjectPermission) {
                throw new NoPermissionException("You have no permissions to delete other people's work items for this project");
            }
        }

        entriesService.deleteEntry(entryId);
    }


}
