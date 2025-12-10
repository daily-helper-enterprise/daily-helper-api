package com.project.daily.model.request;

import lombok.Getter;

@Getter
public class UpdateEntryRequest {
    
    private boolean resolved;
    private String title;
    private String description;
    
}
