package com.example.todo.service.impl;

import com.example.todo.common.errors.IncorrectArgumentException;
import com.example.todo.data.entity.user.User;
import com.example.todo.data.repo.user.UsersRepository;
import com.example.todo.service.UsersAdminService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;

@Service
@RequiredArgsConstructor
public class UserAdminServiceImpl implements UsersAdminService {

    private final UsersRepository usersRepository;


    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public User getOrCreateUser(String email, String name) {
        User user = usersRepository.findOneByEmail(email);

        if (user == null) {
            try {
                return createUser(email, name);
            } catch (ConstraintViolationException ex) {
                throw new IncorrectArgumentException("Invalid request", ex);
            } catch (DataIntegrityViolationException ex) {
                if (ex.getCause() instanceof ConstraintViolationException) {
                    ConstraintViolationException casted = (ConstraintViolationException) ex.getCause();
                    if ("users_unique_email".equals(casted.getConstraintName())) {
                        throw new IncorrectArgumentException("User " + email + " already exists", ex);
                    } else {
                        throw new IncorrectArgumentException("Invalid request", ex);
                    }
                }
                throw ex;
            }
        }

        return user;
    }

    private User createUser(String email, String name) {
        // Check email only
        if (StringUtils.isBlank(email)) {
            throw new IncorrectArgumentException("Email is empty");
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setWeekStart(DayOfWeek.MONDAY);
        user.setIsValidated(false);

        return usersRepository.save(user);
    }


}
