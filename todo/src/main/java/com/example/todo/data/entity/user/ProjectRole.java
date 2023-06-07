package com.example.todo.data.entity.user;

import com.example.todo.data.entity.BaseEntity;
import com.example.todo.data.entity.Project;
import jakarta.persistence.*;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Entity
@Data
@Table(name = "project_roles", schema = "public")
@NamedEntityGraphs({
        @NamedEntityGraph(name = "ProjectRole.project.user", attributeNodes = {@NamedAttributeNode("project"), @NamedAttributeNode("user")}),
        @NamedEntityGraph(name = "ProjectRole.project", attributeNodes = @NamedAttributeNode("project")),
        @NamedEntityGraph(name = "ProjectRole.user", attributeNodes = @NamedAttributeNode("user")),
        @NamedEntityGraph(name = "ProjectRole.projectActions",
                attributeNodes = {@NamedAttributeNode(value = "project", subgraph = "Project.actions")},
                subgraphs = {@NamedSubgraph(name = "Project.actions", attributeNodes = @NamedAttributeNode("actions"))}
        )
})
public class ProjectRole implements BaseEntity {

    @EmbeddedId
    private ProjectRoleIdentity identity;

    @MapsId("project_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @MapsId("user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

}
