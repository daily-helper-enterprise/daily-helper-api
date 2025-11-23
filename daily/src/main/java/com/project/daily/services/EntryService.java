package com.project.daily.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final DailyService dailyService; // NOVO: Injeção do DailyService

    // Construtor atualizado para incluir DailyService
    public EntryService(EntryRepository entryRepository, AuthService authService, DailyService dailyService) {
        this.entryRepository = entryRepository;
        this.authService = authService;
        this.dailyService = dailyService;
    }

    public EntryResponse findById(Long id) {
        Entry entry = entryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entry not found"));
        return toResponse(entry);
    }


    public List<EntryResponse> findToday() {
        var loggedUser = authService.getLoggedUser();
        return entryRepository.findAllByMemberIdAndCreatedToday(loggedUser.getId()).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Cria uma nova Entry, exigindo o ID da equipe selecionada.
     * @param request Dados da Entry.
     * @param teamId ID da equipe ativa do usuário (vindo do frontend).
     */
    @Transactional
    public EntryResponse create(CreateEntryRequest request, Long teamId) {
        // VERIFICAÇÃO DE PRAZO: Usa DailyService com o teamId
        if (!dailyService.isSubmissionAllowed(LocalDate.now(), teamId)) {
            throw new RuntimeException("Submissão negada: O prazo da Daily Meeting para o time selecionado já se encerrou.");
        }

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

    /**
     * Atualiza uma Entry existente, exigindo o ID da equipe selecionada.
     * @param id ID da Entry a ser atualizada.
     * @param request Dados de atualização.
     * @param teamId ID da equipe ativa do usuário (vindo do frontend).
     */
    @Transactional
    public EntryResponse update(Long id, UpdateEntryRequest request, Long teamId) {
        // VERIFICAÇÃO DE PRAZO (US03): Usa DailyService com o teamId
        if (!dailyService.isEditAllowed(id, teamId)) {
            throw new RuntimeException("Edição negada: O prazo para atualização desta Entry já se encerrou.");
        }

        Entry entry = entryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entry not found"));

        entry.setResolved(request.isResolved());

        Entry updated = entryRepository.save(entry);

        return toResponse(updated);
    }

    @Transactional
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