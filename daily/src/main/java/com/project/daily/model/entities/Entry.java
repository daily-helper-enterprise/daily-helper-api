package com.project.daily.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

// Removida importação de EntryTypeEnum pois Entry agora é a submissão completa.

@Entity
@Table(name = "entries")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
@AttributeOverride(name = "id", column = @Column(name = "entry_id")) // Exemplo para clareza
public class Entry extends Base {

    // Ligação com o membro que fez a submissão
    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id", nullable = false)
    private Member member;

    // Ligação com a Daily (o container de data)
    @ManyToOne
    @JoinColumn(name = "daily_id", referencedColumnName = "id", nullable = false)
    private Daily daily;

    // ----------------------------------------------------
    // Campos obrigatórios do formulário Daily (US01)
    // ----------------------------------------------------

    @Column(name = "what_i_did_yesterday", columnDefinition = "TEXT", nullable = false)
    private String yesterday;

    @Column(name = "what_i_will_do_today", columnDefinition = "TEXT", nullable = false)
    private String today;

    // Impedimentos registrados. Pode ser uma string vazia se não houver impedimentos.
    @Column(name = "impediments", columnDefinition = "TEXT", nullable = false)
    private String impediments;

    // Campo de controle para o limite de edição (US03)
    @Column(name = "is_editable")
    private boolean editable = true; // Controlado por lógica de horário limite

    @Column(name = "impediment_resolved")
    private boolean impedimentResolved;
}