package com.example.todo.data.repo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EntriesStats {
    private String project;
    private double hours;

    public EntriesStats(String project, double hours) {
        this.project = project;
        this.hours = hours;
    }

    public String getProject() {
        return project;
    }

    public double getHours() {
        return hours;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public void setHours(double hours) {
        this.hours = hours;
    }
}
