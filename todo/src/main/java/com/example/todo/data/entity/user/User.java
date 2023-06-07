package com.example.todo.data.entity.user;

import com.example.todo.data.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;

@Entity
@Data
@Table(name = "users", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "users_unique_email", columnNames = "email")
})
public class User implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "email")
    private String email;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "validated")
    private Boolean isValidated;

    @NotNull
    @Column(name = "weekStart")
    private DayOfWeek weekStart;
}
