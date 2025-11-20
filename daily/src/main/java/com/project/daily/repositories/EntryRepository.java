package com.project.daily.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.daily.model.entities.Entry;

public interface EntryRepository extends JpaRepository<Entry, Long> {

    @Query(
        value = "SELECT * FROM entries e WHERE e.member_id = :id AND CAST(e.created_at AS DATE) = CURRENT_DATE",
        nativeQuery = true
    )
    List<Entry> findAllByMemberIdAndCreatedToday(@Param("id") Long id);


}
