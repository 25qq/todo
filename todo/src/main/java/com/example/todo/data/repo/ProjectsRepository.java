package com.example.todo.data.repo;

import com.example.todo.data.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectsRepository extends JpaRepository<Project, Long>, BaseRepo {

    boolean existsByTitle(String title);

    boolean existsByTitleAndIdNot(String title, Long projectId);

    Project findOneByTitle(String title);

}
