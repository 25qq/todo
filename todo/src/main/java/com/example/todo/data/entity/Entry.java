package com.example.todo.data.entity;

import com.example.todo.data.entity.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Data
@Table(name = "entries", schema = "public")
@NamedEntityGraphs({
        @NamedEntityGraph(name = "Entry.actionProject",
                attributeNodes = {@NamedAttributeNode(value = "action", subgraph = "Action.project")},
                subgraphs = {@NamedSubgraph(name = "Action.project", attributeNodes = @NamedAttributeNode("project"))}
        ),
        @NamedEntityGraph(name = "Entry.actionProject.user",
                attributeNodes = {@NamedAttributeNode(value = "action", subgraph = "Action.project"), @NamedAttributeNode(value = "user")},
                subgraphs = {@NamedSubgraph(name = "Action.project", attributeNodes = @NamedAttributeNode("project"))}
        )})
public class Entry implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "obs")
    private LocalDate obs;

    @NotNull
    @Column(name = "hours", scale = 1)
    private Double hours;

    @NotNull
    @Column(name = "title")
    private String title;

    @NotNull
    @Column(name = "closed")
    private Boolean isClosed;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "action_id")
    private Action action;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "update_at", updatable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
