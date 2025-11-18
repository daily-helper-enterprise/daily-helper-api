package com.project.daily.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.daily.model.entities.Entry;

public interface EntryRepository extends JpaRepository<Entry, Long> {


}
