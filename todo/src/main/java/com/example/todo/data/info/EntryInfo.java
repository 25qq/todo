package com.example.todo.data.info;

import com.example.todo.data.entity.Entry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntryInfo {
    private LocalDate obs;
    private Double hours;
    private String title;
    private Long actionId;

    public EntryInfo(Entry entry) {
        setObs(entry.getObs());
        setTitle(entry.getTitle());
        setHours(entry.getHours());
        if (entry.getAction() != null) {
            setActionId(entry.getAction().getId());
        }
    }
}
