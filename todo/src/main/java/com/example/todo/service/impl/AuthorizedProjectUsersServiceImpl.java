package com.example.todo.service.impl;

import com.example.todo.common.errors.NoPermissionException;
import com.example.todo.data.entity.Project;
import com.example.todo.data.entity.user.ProjectRole;
import com.example.todo.data.entity.user.ProjectRoleIdentity;
import com.example.todo.data.entity.user.User;
import com.example.todo.data.enums.ProjectPermissionType;
import com.example.todo.data.info.ProjectRoleInfo;
import com.example.todo.data.repo.ProjectsRepository;
import com.example.todo.data.repo.user.ProjectRolesRepository;
import com.example.todo.data.utils.RolePermissionsHelper;
import com.example.todo.service.AuthorizedProjectUsersService;
import com.example.todo.service.ProjectUsersService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AuthorizedProjectUsersServiceImpl implements AuthorizedProjectUsersService {
    private final RolePermissionsHelper rolePermissionsHelper;
    private final ProjectRolesRepository projectRolesRepository;
    private final ProjectUsersService projectUsersService;
    private final ProjectsRepository projectsRepository;



    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ProjectRole createProjectRole(@NotNull User user, @NotNull Long projectId, @NotNull Long userId, @NotNull ProjectRoleInfo projectRoleInfo) {
        Project project = projectsRepository.getOne(projectId);

        if (!rolePermissionsHelper.hasProjectPermission(user, project, ProjectPermissionType.EDIT_PROJECT_USERS)) {
            throw new NoPermissionException("You have no permissions to update this project");
        }

        if (user.getId().equals(userId)) {
            throw new NoPermissionException("Project user cannot be updated by the same user");
        }

        return projectUsersService.createProjectRole(projectId, userId, projectRoleInfo);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ProjectRole updateProjectRole(@NotNull User user, @NotNull ProjectRoleIdentity identity, @NotNull ProjectRoleInfo projectRoleInfo) {
        ProjectRole projectRole = projectRolesRepository.getOne(identity);

        if (!rolePermissionsHelper.hasProjectPermission(user, projectRole.getProject(), ProjectPermissionType.EDIT_PROJECT_USERS)) {
            throw new NoPermissionException("You have no permissions to update this project");
        }

        if (user.getId().equals(identity.getUserId())) {
            throw new NoPermissionException("Project user cannot be updated by the same user");
        }

        return projectUsersService.updateProjectRole(identity, projectRoleInfo);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public boolean deleteOrDeactivateProjectRole(@NotNull User user, @NotNull ProjectRoleIdentity projectRoleIdentity) {
        ProjectRole projectRole = projectRolesRepository.findFullByIdentity(projectRoleIdentity);

        if (!rolePermissionsHelper.hasProjectPermission(user, projectRole.getProject(), ProjectPermissionType.EDIT_PROJECT_USERS)) {
            throw new NoPermissionException("You have no permissions to update this project");
        }

        if (user.getId().equals(projectRole.getUser().getId())) {
            throw new NoPermissionException("Project user cannot be updated by the same user");
        }

        return projectUsersService.deleteOrDeactivateProjectRole(projectRoleIdentity);
    }
}
