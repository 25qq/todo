package com.example.todo.data.info;

import com.example.todo.data.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private String name;
    private DayOfWeek weekStart;

    public UserInfo(User user) {
        setWeekStart(user.getWeekStart());
        setName(user.getName());
    }
}
