package com.example.todo.data.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Embeddable
public class ClosedDayIdentity implements Serializable {

    @NotNull
    @Column(name = "obs")
    private LocalDate obs;

    @NotNull
    @Column(name = "project_id")
    private Long projectId;
}