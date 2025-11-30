package com.project.daily.model.request;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamRequest {

    private String name;
    private String description;

    // ID do Scrum Master
    private Long scrumMasterId;

    // Lista de IDs de membros
    private List<Long> membersIds;
}
