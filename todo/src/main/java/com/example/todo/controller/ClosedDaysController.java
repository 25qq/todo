package com.example.todo.controller;

import com.example.todo.common.errors.NoPermissionException;
import com.example.todo.data.entity.user.User;
import com.example.todo.security.HasUser;
import com.example.todo.service.AuthorizedClosedDaysService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@AllArgsConstructor
@RequestMapping("/ClosedDay")
public class ClosedDaysController {

    private final AuthorizedClosedDaysService authorizedClosedDaysService;
    private final HasUser hasUser;

    @PostMapping("/{projectId}/openDays")
    public ResponseEntity<String> openDays(@PathVariable Long projectId,
                                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

       User user  = hasUser.getUser();

        try {
            authorizedClosedDaysService.openDays(user, projectId, from, to);
            return ResponseEntity.ok("Days opened successfully.");
        } catch (NoPermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
        }

    }

    @PostMapping("/{projectId}/closeDays")
    public ResponseEntity<String> closeDays(@PathVariable Long projectId,
                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

       User user = hasUser.getUser();

        try {
            authorizedClosedDaysService.closeDays(user, projectId, from, to);
            return ResponseEntity.ok("Days closed successfully.");
        } catch (NoPermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
        }

    }



}
