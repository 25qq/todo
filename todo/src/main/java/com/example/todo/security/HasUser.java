package com.example.todo.security;


import com.example.todo.data.entity.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

public interface HasUser {

    default User getUser() {
        return SecurityUtils.getCurrentUser()
                .map(UserPrincipal::getUser)
                .orElseThrow(CurrentUserNotFoundException::new);
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    class CurrentUserNotFoundException extends RuntimeException {
    }


}
