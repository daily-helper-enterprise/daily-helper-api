package com.project.daily.repositories;

import com.project.daily.model.entities.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SprintRepository extends JpaRepository<Sprint, Long> {

    // Métodos específicos para Sprint, se necessário, podem ser adicionados aqui.
}