package com.example.todo.service;


import com.example.todo.data.entity.user.ProjectRole;
import com.example.todo.data.entity.user.ProjectRoleIdentity;
import com.example.todo.data.info.ProjectRoleInfo;
import jakarta.validation.constraints.NotNull;

public interface ProjectUsersService {
    ProjectRole createProjectRole(@NotNull Long projectId, @NotNull Long userId, @NotNull ProjectRoleInfo projectRole);

    ProjectRole updateProjectRole(@NotNull ProjectRoleIdentity identity, @NotNull ProjectRoleInfo projectRole);

    boolean deleteOrDeactivateProjectRole(@NotNull ProjectRoleIdentity projectRoleIdentity);
}
