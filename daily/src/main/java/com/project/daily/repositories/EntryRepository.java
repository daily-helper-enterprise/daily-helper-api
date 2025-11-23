package com.project.daily.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.daily.model.entities.Entry;
import com.project.daily.model.entities.Member; // Import necessário

public interface EntryRepository extends JpaRepository<Entry, Long> {

    @Query(
            value = "SELECT * FROM entries e WHERE e.member_id = :id AND CAST(e.created_at AS DATE) = CURRENT_DATE AND e.removed_at IS NULL",
            nativeQuery = true
    )
    List<Entry> findAllByMemberIdAndCreatedToday(@Param("id") Long id);

    /**
     * NOVO: Busca todas as Entries de um Membro criadas em um determinado intervalo de tempo.
     * Necessário para algumas funcionalidades avançadas de Daily/Sprint.
     */
    List<Entry> findByMemberAndCreatedAtBetween(Member member, LocalDateTime startDateTime, LocalDateTime endDateTime);
}