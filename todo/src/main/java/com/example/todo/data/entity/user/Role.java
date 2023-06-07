package com.example.todo.data.entity.user;

import com.example.todo.data.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Data
@Table(name = "roles", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "roles_unique_code", columnNames = "code"),
        @UniqueConstraint(name = "roles_unique_title", columnNames = "title")
})
@NamedEntityGraphs({
        @NamedEntityGraph(name = "Role.permissions", attributeNodes = @NamedAttributeNode("permissions"))
})
public class Role implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "code")
    private String code;

    @NotNull
    @Column(name = "title")
    private String title;

    @NotNull
    @Column(name = "description")
    private String description;

    @NotNull
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "roles_permissions",
            schema = "public",
            joinColumns = {@JoinColumn(name = "role_id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_id")})
    private Set<Permission> permissions;
}
