package com.project.daily.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateEntryRequest {

    // O que fiz ontem (US01)
    @NotBlank(message = "O campo 'Ontem' é obrigatório.")
    private String yesterday;

    // O que farei hoje (US01)
    @NotBlank(message = "O campo 'Hoje' é obrigatório.")
    private String today;

    /*
     * Impedimentos registrados (US01).
     */
    @NotNull(message = "O campo 'Impedimentos' é obrigatório.")
    private String impediments;
}