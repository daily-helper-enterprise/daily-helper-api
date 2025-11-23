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
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/today")
    public List<EntryResponse> getToday() {
        return entryService.findToday();
    }

    @GetMapping("/{id}")
    public EntryResponse getById(@PathVariable Long id) {
        return entryService.findById(id);
    }

    /**
     * Rota de Criação: Agora exige o teamId na URL, pois a Daily (e seu prazo)
     * são definidos pelo contexto do time selecionado.
     * Endpoint: POST /entries/team/{teamId}
     */
    @PostMapping("/team/{teamId}")
    public ResponseEntity<EntryResponse> create(
            @PathVariable Long teamId, // NOVO: Captura o teamId da URL
            @RequestBody CreateEntryRequest request) {

        // Chama o serviço passando o teamId
        EntryResponse entry = entryService.create(request, teamId);
        return ResponseEntity.status(HttpStatus.CREATED).body(entry);
    }

    /**
     * Rota de Atualização: Exige teamId na URL para verificação do prazo (DailyService.isEditAllowed).
     * Endpoint: PUT /entries/{id}/team/{teamId}
     */
    @PutMapping("/{id}/team/{teamId}")
    public ResponseEntity<EntryResponse> update(
            @PathVariable Long id,
            @PathVariable Long teamId, // NOVO: Captura o teamId da URL
            @RequestBody UpdateEntryRequest request) {

        // Chama o serviço passando o teamId
        EntryResponse entry = entryService.update(id, request, teamId);
        return ResponseEntity.ok(entry);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        // A deleção lógica não depende do prazo do time.
        entryService.delete(id);  // deleção lógica
        return ResponseEntity.noContent().build();
    }


}