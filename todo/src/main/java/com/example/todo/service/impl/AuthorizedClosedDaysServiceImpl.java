package com.example.todo.service.impl;

import com.example.todo.common.errors.NoPermissionException;
import com.example.todo.data.entity.Project;
import com.example.todo.data.entity.user.User;
import com.example.todo.data.enums.ProjectPermissionType;
import com.example.todo.data.repo.ProjectsRepository;
import com.example.todo.data.utils.RolePermissionsHelper;
import com.example.todo.service.AuthorizedClosedDaysService;
import com.example.todo.service.ClosedDaysService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthorizedClosedDaysServiceImpl implements AuthorizedClosedDaysService {
    private final ClosedDaysService closedDaysService;
    private final ProjectsRepository projectsRepository;
    private final RolePermissionsHelper rolePermissionsHelper;


    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public void openDays(@NotNull User user, @NotNull Long projectId, @NotNull LocalDate from, @NotNull LocalDate to) {
        Project project = projectsRepository.getOne(projectId);
        if (!rolePermissionsHelper.hasProjectPermission(user, project, ProjectPermissionType.VIEW_PROJECT_LOGS)) {
            throw new NoPermissionException("You have no permissions to update this project");
        }

        closedDaysService.openDays(projectId, from, to);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public void closeDays(@NotNull User user, @NotNull Long projectId, @NotNull LocalDate from, @NotNull LocalDate to) {
        Project project = projectsRepository.getOne(projectId);
        if (!rolePermissionsHelper.hasProjectPermission(user, project, ProjectPermissionType.VIEW_PROJECT_LOGS)) {
            throw new NoPermissionException("You have no permissions to update this project");
        }

        closedDaysService.closeDays(projectId, from, to);
    }
}
