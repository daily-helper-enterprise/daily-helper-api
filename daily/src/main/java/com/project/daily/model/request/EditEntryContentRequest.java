package com.project.daily.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EditEntryContentRequest {

    // Conteúdo do "O que fiz ontem"
    @NotBlank(message = "O campo 'Ontem' é obrigatório para a edição.")
    private String yesterday;

    // Conteúdo do "O que farei hoje"
    @NotBlank(message = "O campo 'Hoje' é obrigatório para a edição.")
    private String today;

    /*
     * Conteúdo dos impedimentos. Deve ser submetido (não nulo), mas pode ser vazio ("")
     * caso o membro não tenha impedimentos.
     */
    @NotNull(message = "O campo 'Impedimentos' é obrigatório para a edição.")
    private String impediments;
}