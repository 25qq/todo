package com.example.todo.data.info;

import com.example.todo.data.entity.user.ProjectRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRoleInfo {
    private Long roleId;

    public ProjectRoleInfo(ProjectRole bean) {
        setRoleId(bean.getRole().getId());
    }
}
