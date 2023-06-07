package com.example.todo.service;


import com.example.todo.data.entity.user.User;
import com.example.todo.data.info.UserInfo;
import jakarta.validation.constraints.NotNull;

public interface AuthorizedUsersService {
    User updateUser(@NotNull User user, @NotNull Long userId, @NotNull UserInfo userInfo);

    boolean validate(@NotNull User user, @NotNull Long userId, @NotNull String name);
}
