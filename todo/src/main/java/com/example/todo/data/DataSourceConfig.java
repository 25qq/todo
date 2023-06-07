package com.example.todo.data;

import com.example.todo.data.entity.BaseEntity;
import com.example.todo.data.repo.BaseRepo;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = BaseRepo.class)
@EntityScan(basePackageClasses = {BaseEntity.class})
public class DataSourceConfig {
}
