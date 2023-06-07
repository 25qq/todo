package com.example.todo.controller;

import com.example.todo.common.errors.NoPermissionException;
import com.example.todo.data.entity.Action;
import com.example.todo.data.entity.user.User;
import com.example.todo.data.info.ActionInfo;
import com.example.todo.security.HasUser;
import com.example.todo.service.AuthorizedProjectActionsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/ProjectAction")
public class ActionController {

    private final AuthorizedProjectActionsService authorizedProjectActionsService;
    private final HasUser hasUser;

    @PostMapping("/{projectId}/create")
    public ResponseEntity<Action> createAction(@RequestBody ActionInfo actionInfo, @PathVariable Long projectId) {

        User user = hasUser.getUser();

        try {
            Action createdAction = authorizedProjectActionsService.createAction(user, projectId, actionInfo);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAction);
        } catch (NoPermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    @PutMapping("/{actionId}/update")
    public ResponseEntity<Action> updateAction(@RequestBody ActionInfo actionInfo, @PathVariable Long actionId) {

        User user = hasUser.getUser();

        try {
            Action updatedAction = authorizedProjectActionsService.updateAction(user, actionId, actionInfo);
            return ResponseEntity.ok(updatedAction);
        } catch (NoPermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    @PutMapping("/{actionId}/activate")
    public ResponseEntity<String> activateAction(@PathVariable Long actionId) {

       User user = hasUser.getUser();

        try {
            authorizedProjectActionsService.activateAction(user, actionId);
            return ResponseEntity.ok("Action activated successfully.");
        } catch (NoPermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
        }

    }

    @DeleteMapping("/{actionId}/deleteOrDeactivateAction")
    public ResponseEntity<String> deleteOrDeactivateAction(@PathVariable Long actionId) {

        User user = hasUser.getUser();

        try {
            boolean deleted = authorizedProjectActionsService.deleteOrDeactivateAction(user, actionId);
            if (deleted) {
                return ResponseEntity.ok("Action deleted successfully.");
            } else {
                return ResponseEntity.ok("Action deactivated successfully.");
            }
        } catch (NoPermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
        }

    }


}
