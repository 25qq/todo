package com.example.todo.service.impl;

import com.example.todo.common.errors.NoPermissionException;
import com.example.todo.data.entity.Project;
import com.example.todo.data.entity.user.User;
import com.example.todo.data.enums.ProjectPermissionType;
import com.example.todo.data.enums.ProjectRoleType;
import com.example.todo.data.info.ProjectInfo;
import com.example.todo.data.info.ProjectRoleInfo;
import com.example.todo.data.repo.ProjectsRepository;
import com.example.todo.data.repo.user.RolesRepository;
import com.example.todo.data.utils.RolePermissionsHelper;
import com.example.todo.service.AuthorizedProjectsService;
import com.example.todo.service.ProjectService;
import com.example.todo.service.ProjectUsersService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AuthorizedProjectServiceImpl implements AuthorizedProjectsService {
    private final RolePermissionsHelper rolePermissionsHelper;
    private final ProjectsRepository projectsRepository;
    private final ProjectService projectsService;
    private final RolesRepository rolesRepository;
    private final ProjectUsersService projectUsersService;


    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public Project createProject(@NotNull User user, @NotNull ProjectInfo projectInfo) {
        Project project = projectsService.createProject(projectInfo);

        // Add admin project role to user
        ProjectRoleInfo projectRoleInfo = new ProjectRoleInfo();
        projectRoleInfo.setRoleId(rolesRepository.findOneByCode(ProjectRoleType.ADMIN.getCode()).getId());
        projectUsersService.createProjectRole(project.getId(), user.getId(), projectRoleInfo);

        return project;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public Project updateProject(@NotNull User user, @NotNull Long projectId, @NotNull ProjectInfo projectInfo) {
        Project project = projectsRepository.getOne(projectId);
        if (!rolePermissionsHelper.hasProjectPermission(user, project, ProjectPermissionType.EDIT_PROJECT_INFO)) {
            throw new NoPermissionException("You have no permissions to update this project");
        }

        return projectsService.updateProject(projectId, projectInfo);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public void activateProject(@NotNull User user, Long projectId) {
        Project project = projectsRepository.getOne(projectId);
        if (!rolePermissionsHelper.hasProjectPermission(user, project, ProjectPermissionType.EDIT_PROJECT_INFO)) {
            throw new NoPermissionException("You have no permissions to update this project");
        }

        projectsService.activateProject(projectId);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public boolean deleteOrDeactivateProject(@NotNull User user, @NotNull Long projectId) {
        Project project = projectsRepository.findById(projectId).get();
        if (!rolePermissionsHelper.hasProjectPermission(user, project, ProjectPermissionType.EDIT_PROJECT_INFO)) {
            throw new NoPermissionException("You have no permissions to update this project");
        }

        return projectsService.deleteOrDeactivateProject(projectId);
    }
}
