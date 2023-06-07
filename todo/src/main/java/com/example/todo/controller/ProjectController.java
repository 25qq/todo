package com.example.todo.controller;

import com.example.todo.common.errors.IncorrectArgumentException;
import com.example.todo.common.errors.NoPermissionException;
import com.example.todo.data.entity.Project;
import com.example.todo.data.entity.user.User;
import com.example.todo.data.info.ProjectInfo;
import com.example.todo.security.HasUser;
import com.example.todo.service.AuthorizedProjectsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final HasUser hasUser;
    private final AuthorizedProjectsService authorizedProjectsService;


    @PostMapping("/create")
    public ResponseEntity<Project> createProject(@RequestBody ProjectInfo projectInfo) {
        User user = hasUser.getUser();
        Project createdProject = authorizedProjectsService.createProject(user, projectInfo);

        return ResponseEntity.ok(createdProject);
    }

    @PutMapping("/{projectId}/update")
    public ResponseEntity<Project> updateProject(
            @PathVariable("projectId") Long projectId,
            @RequestBody @Valid ProjectInfo projectInfo) {

        User user = hasUser.getUser();

        try {
            Project updatedProject = authorizedProjectsService.updateProject(user, projectId, projectInfo);
            return ResponseEntity.ok(updatedProject);
        } catch (IncorrectArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

    }

    @PostMapping("/{projectId}/activate")
    public ResponseEntity<String> activateProject(@PathVariable Long projectId) {

        User user = hasUser.getUser();

        authorizedProjectsService.activateProject(user, projectId);

        return ResponseEntity.ok("Project activated successfully");

    }

    @DeleteMapping("/{projectId}/deleteOrDeactivateProject")
    public ResponseEntity<String> deleteOrDeactivateProject(@RequestBody User user, @PathVariable Long projectId) {

        try {
            boolean deleted = authorizedProjectsService.deleteOrDeactivateProject(user, projectId);
            if (deleted) {
                return ResponseEntity.ok("Project deleted successfully.");
            } else {
                return ResponseEntity.ok("Project deactivated successfully.");
            }
        } catch (NoPermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
        }

    }



}

