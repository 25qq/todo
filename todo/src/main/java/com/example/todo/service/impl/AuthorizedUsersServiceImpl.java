package com.example.todo.service.impl;

import com.example.todo.common.errors.NoPermissionException;
import com.example.todo.data.entity.user.User;
import com.example.todo.data.info.UserInfo;
import com.example.todo.service.AuthorizedUsersService;
import com.example.todo.service.UsersService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AuthorizedUsersServiceImpl implements AuthorizedUsersService {

    private final UsersService usersService;


    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public User updateUser(@NotNull User user, @NotNull Long userId, @NotNull UserInfo userInfo) {
        if (!user.getId().equals(userId)) {
            throw new NoPermissionException("User can be updated by the same user only");
        }

        return usersService.updateUser(userId, userInfo);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public boolean validate(@NotNull User user, @NotNull Long userId, @NotNull String name) {
        if (!user.getId().equals(userId)) {
            throw new NoPermissionException("User can be updated by the same user only");
        }

        return usersService.validate(userId, name);
    }



}
