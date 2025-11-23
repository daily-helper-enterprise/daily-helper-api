package com.project.daily.services;

import com.project.daily.model.entities.Daily;
import com.project.daily.model.entities.Entry;
import com.project.daily.model.entities.Member;
import com.project.daily.model.entities.Team;
import com.project.daily.repositories.DailyRepository;
import com.project.daily.repositories.EntryRepository;
import com.project.daily.repositories.TeamRepository; // NOVO: Injeção do TeamRepository
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DailyService {

    private final DailyRepository dailyRepository;
    private final EntryRepository entryRepository;
    private final AuthService authService; // Para obter o Member logado
    private final TeamRepository teamRepository; // NOVO: Para verificar se o membro pertence ao time

    // =========================================================================
    // Métodos de Suporte
    // =========================================================================

    /**
     * Obtém a Daily Meeting de uma equipe específica para uma data específica.
     *
     * @param date Data da Daily Meeting.
     * @param teamId ID da equipe.
     * @return A Daily Meeting correspondente, se encontrada.
     */
    private Optional<Daily> getDailyByDateAndTeam(LocalDate date, Long teamId) {
        return dailyRepository.findByDailyDateAndTeamId(date, teamId);
    }

    /**
     * Verifica se o membro logado pertence ao time fornecido.
     * @param teamId ID do time.
     */
    private void validateMemberBelongsToTeam(Long teamId) {
        Member member = authService.getLoggedUser();
        if (member == null) {
            throw new RuntimeException("Usuário não autenticado.");
        }

        // Busca o time pelo ID
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Time não encontrado."));

        // Verifica se o membro está no conjunto de membros do time
        boolean isMemberOfTeam = team.getMembers().stream()
                .anyMatch(m -> m.getId().equals(member.getId()));

        if (!isMemberOfTeam) {
            throw new RuntimeException("Acesso negado: O usuário não pertence a este time.");
        }
    }


    // =========================================================================
    // Lógica de Prazo (US03)
    // =========================================================================

    /**
     * Verifica se a submissão/criação de uma nova Entry é permitida para a Daily de hoje
     * NO CONTEXTO DE UMA EQUIPE SELECIONADA.
     *
     * @param date Data da Daily Meeting (normalmente LocalDate.now()).
     * @param teamId ID da equipe selecionada pelo usuário.
     * @return True se a submissão for permitida (dentro do prazo), False caso contrário.
     */
    @Transactional(readOnly = true)
    public boolean isSubmissionAllowed(LocalDate date, Long teamId) {
        validateMemberBelongsToTeam(teamId); // Garante que o usuário tem permissão para esta equipe

        Optional<Daily> dailyOpt = getDailyByDateAndTeam(date, teamId);

        if (dailyOpt.isEmpty()) {
            return false;
        }

        Daily daily = dailyOpt.get();
        LocalTime deadline = daily.getSubmissionDeadlineTime();

        if (deadline == null) {
            return true;
        }

        LocalTime currentTime = LocalTime.now();

        return !currentTime.isAfter(deadline);
    }

    /**
     * Verifica se uma Entry existente ainda pode ser editada (US03).
     *
     * @param entryId ID da Entry a ser editada.
     * @param teamId ID da equipe que o usuário tem ativa (para buscar a Daily correta).
     * @return True se a edição for permitida, False caso contrário.
     */
    @Transactional(readOnly = true)
    public boolean isEditAllowed(Long entryId, Long teamId) {
        validateMemberBelongsToTeam(teamId); // Garante que o usuário tem permissão

        Entry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new RuntimeException("Entry não encontrada para verificação de prazo."));

        // Também é essencial garantir que a Entry pertença ao membro logado
        if (!entry.getMember().getId().equals(authService.getLoggedUser().getId())) {
            throw new RuntimeException("Acesso negado: Você não pode editar uma Entry de outro usuário.");
        }

        // Encontra a Daily Meeting pelo dia de criação da Entry e pelo Time
        LocalDate entryDate = entry.getCreatedAt().toLocalDate();

        Optional<Daily> dailyOpt = getDailyByDateAndTeam(entryDate, teamId);

        if (dailyOpt.isEmpty()) {
            return false;
        }

        Daily daily = dailyOpt.get();
        LocalTime deadline = daily.getSubmissionDeadlineTime();

        if (deadline == null) {
            return true;
        }

        LocalTime currentTime = LocalTime.now();

        return !currentTime.isAfter(deadline);
    }
}