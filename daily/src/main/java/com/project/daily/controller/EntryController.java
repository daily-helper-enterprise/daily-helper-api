package com.project.daily.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.daily.model.request.CreateEntryRequest;
import com.project.daily.model.request.UpdateEntryRequest;
import com.project.daily.model.response.EntryResponse;
import com.project.daily.services.EntryService;

@RestController
@RequestMapping("/entries")
public class EntryController {
    
    private final EntryService entryService;

    public EntryController(EntryService entryService) {
        this.entryService = entryService;
    }

    @GetMapping
    public List<EntryResponse> getAll() {
        return entryService.findAll();
    }

    @GetMapping("/{id}")
    public EntryResponse getById(@PathVariable Long id) {
        return entryService.findById(id);
    }

    @PostMapping
    public ResponseEntity<EntryResponse> create(@RequestBody CreateEntryRequest request) {
        EntryResponse entry = entryService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(entry);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntryResponse> update(
            @PathVariable Long id,
            @RequestBody UpdateEntryRequest request) {

        EntryResponse entry = entryService.update(id, request);
        return ResponseEntity.ok(entry);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        entryService.delete(id);  // deleção lógica
        return ResponseEntity.noContent().build();
    }


}
