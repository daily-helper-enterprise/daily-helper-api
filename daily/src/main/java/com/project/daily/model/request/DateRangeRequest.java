package com.project.daily.model.request;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DateRangeRequest {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}