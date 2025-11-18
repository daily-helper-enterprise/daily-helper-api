package com.project.daily.model.response;

import com.project.daily.enums.EntryTypeEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EntryResponse {
    private Long id;
    private Long memberId;
    private EntryTypeEnum type;
    private String description;
    private boolean resolved;
}