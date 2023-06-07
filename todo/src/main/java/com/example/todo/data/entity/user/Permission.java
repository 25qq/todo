package com.example.todo.data.entity.user;

import com.example.todo.data.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Entity
@Data
@Table(name = "permissions", schema = "public")
public class Permission implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "code", unique = true)
    private String code;
}
