package com.example.todo.controller;

import com.example.todo.common.errors.NoPermissionException;
import com.example.todo.data.entity.user.User;
import com.example.todo.data.info.UserInfo;
import com.example.todo.security.HasUser;
import com.example.todo.service.AuthorizedUsersService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/User")
public class UserController {

    private final AuthorizedUsersService authorizedUsersService;
    private final HasUser hasUser;


    @PutMapping("/{userId}/update")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody UserInfo userInfo) {

        User user = hasUser.getUser();

        try {
            if (!user.getId().equals(userId)) {
                throw new NoPermissionException("User can be updated by the same user only");
            }

            User updatedUser = authorizedUsersService.updateUser(user, userId, userInfo);
            return ResponseEntity.ok(updatedUser);
        } catch (NoPermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    @PostMapping("/{userId}/validate")
    public ResponseEntity<Boolean> validate(@PathVariable Long userId, @RequestParam String name) {

        User user = hasUser.getUser();

        try {
            if (!user.getId().equals(userId)) {
                throw new NoPermissionException("User can be updated by the same user only");
            }

            boolean isValid = authorizedUsersService.validate(user, userId, name);
            return ResponseEntity.ok(isValid);
        } catch (NoPermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(false);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }

    }



}
