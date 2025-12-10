package com.project.daily.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TeamMembersResponse {
    private String name;
    private Long id;
}
