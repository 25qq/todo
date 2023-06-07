package com.example.todo.data.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Embeddable
public class ProjectRoleIdentity implements Serializable {

    @NotNull
    @Column(name = "project_id")
    private Long projectId;

    @NotNull
    @Column(name = "user_id")
    private Long userId;
}
