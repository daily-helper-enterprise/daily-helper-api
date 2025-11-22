package com.project.daily.model.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TeamResponse {

    private Long id;
    private String name;
    private String description;

    private String scrumMaster; // nome do scrum master
    private List<String> members; // lista de nomes dos membros
}
