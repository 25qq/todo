package com.example.todo.service.impl;

import com.example.todo.common.errors.IncorrectArgumentException;
import com.example.todo.data.entity.user.User;
import com.example.todo.data.info.UserInfo;
import com.example.todo.data.repo.user.UsersRepository;
import com.example.todo.service.UsersService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UsersService {

    private final UsersRepository usersRepository;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public User updateUser(@NotNull Long userId, @NotNull UserInfo userInfo) {
        // Validate
        preValidate(userInfo);

        User user = usersRepository.getOne(userId);
        user.setName(userInfo.getName());
        user.setWeekStart(userInfo.getWeekStart());

        return usersRepository.save(user);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public boolean validate(@NotNull Long userId, @NotNull String name) {

        boolean validated = true;

        // Validate
        if (StringUtils.isBlank(name)) {
            validated = false;
        }

        if (validated) {
            User user = usersRepository.getOne(userId);
            user.setName(name);
            user.setIsValidated(true);
            return usersRepository.save(user).getIsValidated();
        }

        return false;
    }

    private void preValidate(UserInfo userInfo) {
        if (StringUtils.isBlank(userInfo.getName())) {
            throw new IncorrectArgumentException("Name is empty");
        }

        if (userInfo.getWeekStart() == null) {
            throw new IncorrectArgumentException("Week starting day is empty");
        }
    }


}
