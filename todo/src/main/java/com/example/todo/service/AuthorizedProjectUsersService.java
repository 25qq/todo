package com.example.todo.service;


import com.example.todo.data.entity.user.ProjectRole;
import com.example.todo.data.entity.user.ProjectRoleIdentity;
import com.example.todo.data.entity.user.User;
import com.example.todo.data.info.ProjectRoleInfo;
import jakarta.validation.constraints.NotNull;

public interface AuthorizedProjectUsersService {
    ProjectRole createProjectRole(@NotNull User user, @NotNull Long projectId, @NotNull Long userId, @NotNull ProjectRoleInfo projectRole);

    ProjectRole updateProjectRole(@NotNull User user, @NotNull ProjectRoleIdentity identity, @NotNull ProjectRoleInfo projectRole);

    boolean deleteOrDeactivateProjectRole(@NotNull User user, @NotNull ProjectRoleIdentity projectRoleIdentity);
}
