package com.project.daily.model.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "dailies")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
public class Daily extends Base {

    // Data da Daily Meeting. Será usada para correlacionar com Entry.createdAt.
    @Column(name = "daily_date", nullable = false, unique = true)
    private LocalDate dailyDate;

    // Horário limite para a submissão das Entries (Ex: 17:00).
    @Column(name = "submission_deadline_time")
    private LocalTime submissionDeadlineTime;

    // A Daily pertence a uma única Sprint (US11)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprint_id", referencedColumnName = "id", nullable = false)
    private Sprint sprint;
}