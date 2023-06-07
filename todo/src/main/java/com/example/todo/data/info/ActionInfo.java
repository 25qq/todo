package com.example.todo.data.info;

import com.example.todo.data.entity.Action;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionInfo {
    String title;

    public ActionInfo(Action bean) {
        setTitle(bean.getTitle());
    }
}
