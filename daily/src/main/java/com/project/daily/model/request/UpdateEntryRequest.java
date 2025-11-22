package com.project.daily.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateEntryRequest {

    // Status de resolução do impedimento (US08)
    @NotNull(message = "O status de resolução é obrigatório.")
    private Boolean impedimentResolved;
}