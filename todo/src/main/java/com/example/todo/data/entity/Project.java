package com.example.todo.data.entity;

import jakarta.persistence.*;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;


@Entity
@Data
@Table(name = "projects", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "projects_unique_title", columnNames = "title")
})
public class Project implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title")
    private String title;

    @NotNull
    @Column(name = "active")
    private Boolean isActive;

    @OrderBy("title")
    @OneToMany(mappedBy = "project")
    private List<Action> actions;
}
