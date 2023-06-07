package com.example.todo.data.info;

import com.example.todo.data.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectInfo implements BaseEntity {
    private String title;
}
