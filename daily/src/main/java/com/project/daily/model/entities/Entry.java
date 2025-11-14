package com.project.daily.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import com.project.daily.enums.EntryTypeEnum;


@Entity
@Table(name = "entries")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Entry extends Base {

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private EntryTypeEnum type;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private boolean resolved;
}
