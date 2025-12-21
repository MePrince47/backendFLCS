// entity/EvaluationHebdomadaire.java
// package com.flcs.entity;
package FLCS.GESTION.Models;



import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "evaluations_hebdomadaires")
// @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class EvaluationHebdomadaire extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eleve_id", nullable = false)
    private Eleve eleve;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "niveau_id", nullable = false)
    private Niveau niveau;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enseignant_id", nullable = false)
    private Enseignant enseignant;
    
    @Column(nullable = false)
    private Integer semaine;
    
    @Column(nullable = false)
    private Integer annee;
    
    @Column(name = "date_evaluation")
    private LocalDate dateEvaluation = LocalDate.now();
    
    // Notes (0-20)
    @Column(name = "note_lesen")
    private Double noteLesen;
    
    @Column(name = "note_horen")
    private Double noteHoren;
    
    @Column(name = "note_schreiben")
    private Double noteSchreiben;
    
    @Column(name = "note_grammatik")
    private Double noteGrammatik;
    
    @Column(name = "note_sprechen")
    private Double noteSprechen;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Statut statut = Statut.BROUILLON;
    
    public enum Statut {
        BROUILLON, VALIDE
    }
    
    @Transient
    public Double getMoyenne() {
        double somme = noteLesen + noteHoren + noteSchreiben + noteGrammatik + noteSprechen;
        return somme / 5;
    }
    
    @PrePersist
    @PreUpdate
    public void validateNotes() {
        if (noteLesen < 0 || noteLesen > 20 ||
            noteHoren < 0 || noteHoren > 20 ||
            noteSchreiben < 0 || noteSchreiben > 20 ||
            noteGrammatik < 0 || noteGrammatik > 20 ||
            noteSprechen < 0 || noteSprechen > 20) {
            throw new IllegalArgumentException("Les notes doivent être entre 0 et 20");
        }
    }
}