package com.example.todo.service.impl;


import com.example.todo.common.errors.IncorrectArgumentException;
import com.example.todo.data.entity.Action;
import com.example.todo.data.entity.Project;
import com.example.todo.data.info.ProjectInfo;
import com.example.todo.data.repo.ActionsRepository;
import com.example.todo.data.repo.ClosedDaysRepository;
import com.example.todo.data.repo.ProjectsRepository;
import com.example.todo.data.repo.user.ProjectRolesRepository;
import com.example.todo.service.ProjectActionsService;
import com.example.todo.service.ProjectService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectsRepository projectsRepository;
    private final ProjectActionsService projectActionsService;
    private final ActionsRepository actionsRepository;
    private final ProjectRolesRepository projectRolesRepository;
    private final ClosedDaysRepository closedDaysRepository;



    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public Project createProject(@NotNull ProjectInfo projectInfo) {
        try {
            return doCreateProject(projectInfo);
        } catch (ConstraintViolationException ex) {
            throw new IncorrectArgumentException("Invalid request", ex);
        } catch (DataIntegrityViolationException ex) {
            if (ex.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException casted = (ConstraintViolationException) ex.getCause();
                if ("projects_unique_title".equals(casted.getConstraintName())) {
                    throw new IncorrectArgumentException("Project " + projectInfo.getTitle() + " already exists", ex);
                } else {
                    throw new IncorrectArgumentException("Invalid request", ex);
                }
            }
            throw ex;
        }
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public Project updateProject(@NotNull Long projectId, @NotNull ProjectInfo projectInfo) {
        try {
            return doUpdateProject(projectId, projectInfo);
        } catch (ConstraintViolationException ex) {
            throw new IncorrectArgumentException("Invalid request", ex);
        } catch (DataIntegrityViolationException ex) {
            if (ex.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException casted = (ConstraintViolationException) ex.getCause();
                if ("projects_unique_title".equals(casted.getConstraintName())) {
                    throw new IncorrectArgumentException("Project " + projectInfo.getTitle() + " already exists", ex);
                } else {
                    throw new IncorrectArgumentException("Invalid request", ex);
                }
            }
            throw ex;
        }
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public void activateProject(@NotNull Long projectId) {
        doActivateProject(projectId);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public boolean deleteOrDeactivateProject(@NotNull Long projectId) {
        return doDeleteOrDeactivateProject(projectId);
    }

    private Project doCreateProject(@NotNull ProjectInfo projectInfo) {
        preValidate(projectInfo);

        // Validate
        if (projectsRepository.existsByTitle(projectInfo.getTitle())) {
            throw new IncorrectArgumentException("Project " + projectInfo.getTitle() + " already exists");
        }

        // Create project
        Project project = new Project();
        project.setTitle(projectInfo.getTitle());
        project.setIsActive(true);

        project = projectsRepository.save(project);

        return project;
    }

    private Project doUpdateProject(@NotNull Long projectId, @NotNull ProjectInfo projectInfo) {
        preValidate(projectInfo);

        Project project = projectsRepository.getOne(projectId);

        // Validate
        if (!project.getIsActive()) {
            throw new IncorrectArgumentException("Inactive project can't be updated");
        }

        if (projectsRepository.existsByTitleAndIdNot(projectInfo.getTitle(), projectId)) {
            throw new IncorrectArgumentException("Project " + projectInfo.getTitle() + " already exists");
        }

        // Update
        project.setTitle(projectInfo.getTitle());
        project = projectsRepository.save(project);

        return project;
    }

    private void doActivateProject(@NotNull Long projectId) {
        Project project = projectsRepository.getOne(projectId);

        // Validate
        if (project.getIsActive()) {
            throw new IncorrectArgumentException("Project is already active");
        }

        project.setIsActive(true);
        projectsRepository.save(project);
    }

    public boolean doDeleteOrDeactivateProject(@NotNull Long projectId) {
        Project project = projectsRepository.getOne(projectId);

        // Validate
        if (!project.getIsActive()) {
            throw new IncorrectArgumentException("Project is already inactive");
        }

        // Deactivate project's actions
        List<Action> actions = actionsRepository.findByProjectAndIsActive(project, true);
        for (Action action : actions) {
            projectActionsService.deleteOrDeactivateAction(action.getId());
        }

        //  Check if any actions are left
        if (actionsRepository.existsByProject(project)) {
            // Deactivate project
            project.setIsActive(false);
            projectsRepository.save(project);
            return false;
        }

        // Delete project with roles
        closedDaysRepository.deleteByProject(project);
        projectRolesRepository.deleteByProject(project);
        projectsRepository.deleteById(project.getId());
        return true;
    }

    private void preValidate(@NotNull ProjectInfo project) {
        if (Strings.isBlank(project.getTitle())) {
            throw new IncorrectArgumentException("Project title is empty");
        }
    }
}
