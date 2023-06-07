package com.example.todo.data.repo;

import com.example.todo.data.entity.ClosedDay;
import com.example.todo.data.entity.ClosedDayIdentity;
import com.example.todo.data.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClosedDaysRepository extends JpaRepository<ClosedDay, ClosedDayIdentity>, BaseRepo {

    List<ClosedDay> findByIdentityObsBetween(LocalDate from, LocalDate to);

    List<ClosedDay> findByProject(Project project);

    Long countByProjectAndIdentityObsBetween(Project project, LocalDate from, LocalDate to);

    @Transactional(propagation = Propagation.REQUIRED)
    Long deleteByProjectAndIdentityObsBetween(Project project, LocalDate from, LocalDate to);

    @Transactional(propagation = Propagation.REQUIRED)
    long deleteByProject(Project project);
}
