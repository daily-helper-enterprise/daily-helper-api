package com.project.daily.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.daily.model.entities.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
}
