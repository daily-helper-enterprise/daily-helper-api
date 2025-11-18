package com.project.daily.model.request;


import com.project.daily.enums.EntryTypeEnum;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class CreateEntryRequest {
    private EntryTypeEnum type;
    private String description;
}

