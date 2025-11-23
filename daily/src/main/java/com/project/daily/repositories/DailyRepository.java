package com.project.daily.repositories;

import com.project.daily.model.entities.Daily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DailyRepository extends JpaRepository<Daily, Long> {

    /**
     * BUSCA SEGURA: Obtém a Daily Meeting pela data DENTRO de uma equipe específica.
     * Isso é necessário porque a Daily está ligada à Sprint, que está ligada à Team.
     * @param dailyDate A data da Daily.
     * @param teamId O ID da equipe.
     * @return A Daily Meeting correspondente.
     */
    @Query("SELECT d FROM Daily d JOIN d.sprint s WHERE d.dailyDate = :dailyDate AND s.team.id = :teamId")
    Optional<Daily> findByDailyDateAndTeamId(@Param("dailyDate") LocalDate dailyDate, @Param("teamId") Long teamId);
}