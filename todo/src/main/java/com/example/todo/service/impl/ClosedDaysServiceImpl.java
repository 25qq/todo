package com.example.todo.service.impl;

import com.example.todo.common.errors.IncorrectArgumentException;
import com.example.todo.data.entity.ClosedDay;
import com.example.todo.data.entity.ClosedDayIdentity;
import com.example.todo.data.entity.Project;
import com.example.todo.data.repo.ClosedDaysRepository;
import com.example.todo.data.repo.ProjectsRepository;
import com.example.todo.service.ClosedDaysService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ClosedDaysServiceImpl implements ClosedDaysService {
    private final ClosedDaysRepository closedDaysRepository;
    private final ProjectsRepository projectsRepository;


    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public void openDays(@NotNull Long projectId, @NotNull LocalDate from, @NotNull LocalDate to) {
        Project project = projectsRepository.getOne(projectId);
        if (!project.getIsActive()) {
            throw new IncorrectArgumentException("Inactive project can't be updated");
        }
        closedDaysRepository.deleteByProjectAndIdentityObsBetween(project, from, to);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public void closeDays(@NotNull Long projectId, @NotNull LocalDate from, @NotNull LocalDate to) {
        Project project = projectsRepository.getOne(projectId);
        if (!project.getIsActive()) {
            throw new IncorrectArgumentException("Inactive project can't be updated");
        }

        closedDaysRepository.deleteByProjectAndIdentityObsBetween(project, from, to);

        while (!from.isAfter(to)) {
            ClosedDayIdentity identity = new ClosedDayIdentity();
            identity.setProjectId(project.getId());
            identity.setObs(from);

            ClosedDay day = new ClosedDay();
            day.setIdentity(identity);

            closedDaysRepository.save(day);
            from = from.plusDays(1);
        }

    }



}
