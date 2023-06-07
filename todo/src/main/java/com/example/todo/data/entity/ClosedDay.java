package com.example.todo.data.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "closed_days", schema = "public")
public class ClosedDay {

    @EmbeddedId
    private ClosedDayIdentity identity;

    @MapsId("project_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;
}
