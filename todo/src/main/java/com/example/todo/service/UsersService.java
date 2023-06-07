package com.example.todo.service;

import com.example.todo.data.entity.user.User;
import com.example.todo.data.info.UserInfo;

import javax.validation.constraints.NotNull;

public interface UsersService {
    User updateUser(@NotNull Long userId, @NotNull UserInfo userInfo);

    boolean validate(@NotNull Long userId, @NotNull String name);
}
