package com.example.todo.data.repo.user;

import com.example.todo.data.entity.user.Permission;
import com.example.todo.data.repo.BaseRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionsRepository extends JpaRepository<Permission, Long>, BaseRepo {

    Permission findOneByCode(String code);
}
