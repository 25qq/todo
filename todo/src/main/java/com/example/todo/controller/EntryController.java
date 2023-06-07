package com.example.todo.controller;

import com.example.todo.common.errors.NoPermissionException;
import com.example.todo.data.entity.Entry;
import com.example.todo.data.entity.user.User;
import com.example.todo.data.info.EntryInfo;
import com.example.todo.security.HasUser;
import com.example.todo.service.AuthorizedEntriesService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@AllArgsConstructor
@RequestMapping("/Entry")
public class EntryController {

    private final AuthorizedEntriesService authorizedEntriesService;
    private final HasUser hasUser;


    @PostMapping("/create")
    public ResponseEntity<Entry> createEntry(@RequestBody EntryInfo entryInfo, @RequestParam("userId") Long userId) {

        User user = hasUser.getUser();

        try {
            Entry entry = authorizedEntriesService.createEntry(user, userId, entryInfo);
            return ResponseEntity.ok(entry);
        } catch (NoPermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // or handle the exception according to your needs
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // or handle the exception according to your needs
        }
    }

    @PutMapping("/{entryId}/update")
    public ResponseEntity<Entry> updateEntry(@PathVariable("entryId") Long entryId,
            @RequestBody @Valid EntryInfo entryInfo) {
        User user = hasUser.getUser();
        Entry updatedEntry = authorizedEntriesService.updateEntry(user, entryId, entryInfo);
        return ResponseEntity.ok(updatedEntry);
    }

    @PostMapping("/{projectId}/open")
    public ResponseEntity<Void> openEntries(
            @PathVariable("projectId") Long projectId,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        User user = hasUser.getUser();
        authorizedEntriesService.openEntries(user, projectId, from, to);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{projectId}/close")
    public ResponseEntity<Void> closeEntries(
            @PathVariable("projectId") Long projectId,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        User user = hasUser.getUser();
        authorizedEntriesService.closeEntries(user, projectId, from, to);
        return ResponseEntity.ok().build();
    }




}
