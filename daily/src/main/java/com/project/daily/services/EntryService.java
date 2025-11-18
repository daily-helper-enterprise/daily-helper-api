package com.project.daily.services;



import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.project.daily.model.entities.Entry;
import com.project.daily.model.entities.Member;
import com.project.daily.model.request.CreateEntryRequest;
import com.project.daily.model.request.UpdateEntryRequest;
import com.project.daily.model.response.EntryResponse;
import com.project.daily.repositories.EntryRepository;

@Service
public class EntryService {
    
    private final EntryRepository entryRepository;
    private final AuthService authService;

    public EntryService(EntryRepository entryRepository, AuthService authService) {
        this.entryRepository = entryRepository;
        this.authService = authService;
    }

    public EntryResponse findById(Long id) {
        Entry entry = entryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entry not found"));
        return toResponse(entry);
    }

    public List<EntryResponse> findAll() {
        return entryRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public EntryResponse create(CreateEntryRequest request) {
        Entry entry = new Entry();

        entry.setDescription(request.getDescription());
        entry.setResolved(false);
        entry.setType(request.getType());
        entry.setCreatedAt(LocalDateTime.now());
        Member loggedUser = authService.getLoggedUser();
        entry.setMember(loggedUser);
        Entry saved = entryRepository.save(entry);
        return toResponse(saved);
    }

    public EntryResponse update(Long id, UpdateEntryRequest request) {
        Entry entry = entryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entry not found"));

        entry.setResolved(request.isResolved());

        Entry updated = entryRepository.save(entry);

        return toResponse(updated);
    }

    public void delete(Long id) {
        Entry entry = entryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entry not found"));

        entry.setRemovedAt(LocalDateTime.now());
        entryRepository.save(entry);
    }



    private EntryResponse toResponse(Entry entry) {
        return EntryResponse.builder()
                .id(entry.getId())
                .memberId(entry.getMember().getId())
                .type(entry.getType())
                .description(entry.getDescription())
                .resolved(entry.isResolved())
                .build();
    }

}
