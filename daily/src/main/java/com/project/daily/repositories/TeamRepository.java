package com.project.daily.repositories;

import com.project.daily.model.entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    // O JpaRepository já oferece o findById(Long) que usamos.
}