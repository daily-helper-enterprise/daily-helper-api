package com.project.daily.model.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Role extends Base {

    @Column(name = "name", nullable = false)
    private String name;
}