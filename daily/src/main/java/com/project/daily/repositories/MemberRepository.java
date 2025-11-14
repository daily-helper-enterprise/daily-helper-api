package com.project.daily.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.project.daily.model.entities.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByUsername(String username);
}
