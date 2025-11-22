package com.project.daily.model.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "dailies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Daily extends Base {

    // Data em que a Daily Meeting ocorreu. Usado para US04, US05.
    @Column(name = "daily_date", nullable = false, unique = true)
    private LocalDate dailyDate;

    // A Daily pertence a uma única Sprint (US11)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprint_id", referencedColumnName = "id", nullable = false)
    private Sprint sprint;

    // Uma Daily contém o conjunto de submissões (Entries) dos membros
    @OneToMany(mappedBy = "daily", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Entry> entries = new HashSet<>();
}