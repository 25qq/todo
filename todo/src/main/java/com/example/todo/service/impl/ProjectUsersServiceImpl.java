package com.example.todo.service.impl;

import com.example.todo.common.errors.IncorrectArgumentException;
import com.example.todo.data.entity.Project;
import com.example.todo.data.entity.user.ProjectRole;
import com.example.todo.data.entity.user.ProjectRoleIdentity;
import com.example.todo.data.entity.user.Role;
import com.example.todo.data.enums.ProjectRoleType;
import com.example.todo.data.info.ProjectRoleInfo;
import com.example.todo.data.repo.EntriesRepository;
import com.example.todo.data.repo.ProjectsRepository;
import com.example.todo.data.repo.user.ProjectRolesRepository;
import com.example.todo.data.repo.user.RolesRepository;
import com.example.todo.data.repo.user.UsersRepository;
import com.example.todo.service.ProjectUsersService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ProjectUsersServiceImpl implements ProjectUsersService {
    private final ProjectRolesRepository projectRolesRepository;
    private final EntriesRepository entriesRepository;
    private final RolesRepository rolesRepository;
    private final ProjectsRepository projectsRepository;
    private final UsersRepository usersRepository;


    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ProjectRole createProjectRole(@NotNull Long projectId, @NotNull Long userId, @NotNull ProjectRoleInfo projectRoleInfo) {
        return doCreateProjectRole(projectId, userId, projectRoleInfo);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ProjectRole updateProjectRole(@NotNull ProjectRoleIdentity identity, @NotNull ProjectRoleInfo projectRoleInfo) {
        return doUpdateProjectRole(identity, projectRoleInfo);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public boolean deleteOrDeactivateProjectRole(@NotNull ProjectRoleIdentity projectRoleIdentity) {
        return doDeleteOrDeactivateProjectRole(projectRoleIdentity);
    }

    private ProjectRole doCreateProjectRole(@NotNull Long projectId, @NotNull Long userId, @NotNull ProjectRoleInfo projectRoleInfo) {
        preValidate(projectRoleInfo);

        Role role = rolesRepository.getOne(projectRoleInfo.getRoleId());
        Project project = projectsRepository.getOne(projectId);

        // Validate
        if (projectRolesRepository.existsByUserIdAndProjectId(userId, projectId)) {
            throw new IncorrectArgumentException("Project user already exists");
        }

        if (!project.getIsActive()) {
            throw new IncorrectArgumentException("Inactive project can't be updated");
        }

        // Create
        ProjectRole projectRole = new ProjectRole();
        var id = new ProjectRoleIdentity();
        id.setUserId(userId);
        id.setProjectId(projectId);
        projectRole.setProject(project);
        projectRole.setUser(usersRepository.getOne(userId));
        projectRole.setRole(role);
        projectRole.setIdentity(id);
        projectRole = projectRolesRepository.save(projectRole);

        return projectRole;
    }

    public ProjectRole doUpdateProjectRole(@NotNull ProjectRoleIdentity identity, @NotNull ProjectRoleInfo projectRoleInfo) {
        preValidate(projectRoleInfo);

        ProjectRole projectRole = projectRolesRepository.findFullByIdentity(identity);
        Role role = rolesRepository.getOne(projectRoleInfo.getRoleId());

        if (!projectRole.getProject().getIsActive()) {
            throw new IncorrectArgumentException("Inactive project can't be updated");
        }

        // Update
        projectRole.setRole(role);
        projectRole = projectRolesRepository.save(projectRole);

        return projectRole;
    }

    private boolean doDeleteOrDeactivateProjectRole(@NotNull ProjectRoleIdentity identity) {
        ProjectRole projectRole = projectRolesRepository.findFullByIdentity(identity);
        if (ProjectRoleType.isInactive(projectRole.getRole())) {
            throw new IncorrectArgumentException("Project role is already inactive");
        }

        if (!projectRole.getProject().getIsActive()) {
            throw new IncorrectArgumentException("Inactive project can't be updated");
        }

        // Delete open entries with user
        entriesRepository.deleteByUserAndActionProjectAndIsClosed(projectRole.getUser(), projectRole.getProject(), false);

        //  Check if any entries are left
        if (entriesRepository.existsByUserAndActionProject(projectRole.getUser(), projectRole.getProject())) {
            // Deactivate project role
            Role role = rolesRepository.findOneByCode(ProjectRoleType.INACTIVE.getCode());
            projectRole.setRole(role);
            projectRolesRepository.save(projectRole);
            return false;
        }

        projectRolesRepository.deleteById(projectRole.getIdentity());
        return true;
    }

    private void preValidate(@NotNull ProjectRoleInfo projectRoleInfo) {
        if (projectRoleInfo.getRoleId() == null) {
            throw new IncorrectArgumentException("Project role is empty");
        }
    }
}
