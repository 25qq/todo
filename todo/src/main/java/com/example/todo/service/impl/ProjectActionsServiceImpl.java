package com.example.todo.service.impl;


import com.example.todo.common.errors.IncorrectArgumentException;
import com.example.todo.data.entity.Action;
import com.example.todo.data.entity.Project;
import com.example.todo.data.info.ActionInfo;
import com.example.todo.data.repo.ActionsRepository;
import com.example.todo.data.repo.EntriesRepository;
import com.example.todo.data.repo.ProjectsRepository;
import com.example.todo.service.ProjectActionsService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ProjectActionsServiceImpl implements ProjectActionsService {
    private final ActionsRepository actionsRepository;
    private final EntriesRepository entiesRepository;
    private final ProjectsRepository projectsRepository;


    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public Action createAction(@NotNull Long projectId, @NotNull ActionInfo actionInfo) {
        try {
            return doCreateAction(projectId, actionInfo);
        } catch (ConstraintViolationException ex) {
            throw new IncorrectArgumentException("Invalid request", ex);
        } catch (DataIntegrityViolationException ex) {
            if (ex.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException casted = (ConstraintViolationException) ex.getCause();
                if ("actions_unique_project_id_title".equals(casted.getConstraintName())) {
                    throw new IncorrectArgumentException("Action " + actionInfo.getTitle() + " already exists", ex);
                } else {
                    throw new IncorrectArgumentException("Invalid request", ex);
                }
            }
            throw ex;
        }
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public Action updateAction(@NotNull Long actionId, @NotNull ActionInfo actionInfo) {
        try {
            return doUpdateAction(actionId, actionInfo);
        } catch (ConstraintViolationException ex) {
            throw new IncorrectArgumentException("Invalid request", ex);
        } catch (DataIntegrityViolationException ex) {
            if (ex.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException casted = (ConstraintViolationException) ex.getCause();
                if ("actions_unique_project_id_title".equals(casted.getConstraintName())) {
                    throw new IncorrectArgumentException("Action " + actionInfo.getTitle() + " already exists", ex);
                } else {
                    throw new IncorrectArgumentException("Invalid request", ex);
                }
            }
            throw ex;
        }
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public void activateAction(@NotNull Long actionId) {
        doActivateAction(actionId);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public boolean deleteOrDeactivateAction(@NotNull Long actionId) {
        return doDeleteOrDeactivateAction(actionId);
    }

    private Action doCreateAction(@NotNull Long projectId, @NotNull ActionInfo actionInfo) {
        preValidate(actionInfo);
        Project project = projectsRepository.getOne(projectId);

        if (actionsRepository.existsByProjectAndTitle(project, actionInfo.getTitle())) {
            throw new IncorrectArgumentException("Project action already exists");
        }

        if (!project.getIsActive()) {
            throw new IncorrectArgumentException("Inactive project can't be updated");
        }

        // Create action
        Action action = new Action();
        action.setTitle(actionInfo.getTitle());
        action.setIsActive(true);
        action.setProject(project);
        action = actionsRepository.save(action);

        return action;
    }

    private Action doUpdateAction(@NotNull Long actionId, @NotNull ActionInfo actionInfo) {
        preValidate(actionInfo);

        Action action = actionsRepository.findWithProjectById(actionId);

        // Validate
        if (actionsRepository.existsByProjectAndTitleAndIdNot(action.getProject(), actionInfo.getTitle(), action.getId())) {
            throw new IncorrectArgumentException("Project action already exists");
        }

        if (!action.getProject().getIsActive()) {
            throw new IncorrectArgumentException("Inactive project can't be updated");
        }

        if (!action.getIsActive()) {
            throw new IncorrectArgumentException("Inactive action can't be updated");
        }

        // Create action
        action.setTitle(actionInfo.getTitle());
        action = actionsRepository.save(action);

        return action;
    }

    private void doActivateAction(@NotNull Long actionId) {
        Action action = actionsRepository.findWithProjectById(actionId);

        // Validate
        if (action.getIsActive()) {
            throw new IncorrectArgumentException("Action is already active");
        }

        if (!action.getProject().getIsActive()) {
            throw new IncorrectArgumentException("Inactive project can't be updated");
        }

        action.setIsActive(true);
        actionsRepository.save(action);
    }

    private boolean doDeleteOrDeactivateAction(@NotNull Long actionId) {
        Action action = actionsRepository.findWithProjectById(actionId);

        if (!action.getIsActive()) {
            throw new IncorrectArgumentException("Action is already inactive");
        }

        if (!action.getProject().getIsActive()) {
            throw new IncorrectArgumentException("Inactive project can't be updated");
        }

        // Delete open entries with action
        entiesRepository.deleteByActionAndIsClosed(action, false);

        //  Check if any entries are left
        if (entiesRepository.existsByAction(action)) {
            // Deactivate action
            action.setIsActive(false);
            actionsRepository.save(action);
            return false;
        }

        actionsRepository.deleteById(action.getId());
        return true;
    }

    private void preValidate(@NotNull ActionInfo actionInfo) {
        if (Strings.isBlank(actionInfo.getTitle())) {
            throw new IncorrectArgumentException("Action title is empty");
        }
    }
}
