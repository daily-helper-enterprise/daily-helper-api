package com.project.daily.model.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberTeamResponse {
    private Long id;
    private String name;
    private String description;
    private boolean scrumMaster;
}
