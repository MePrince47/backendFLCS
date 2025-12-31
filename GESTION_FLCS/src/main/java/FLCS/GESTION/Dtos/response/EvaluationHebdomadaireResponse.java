package FLCS.GESTION.Dtos.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class EvaluationHebdomadaireResponse {
    private Long id;
    private String uuid;
    private Long eleveId;
    private String eleveNom;
    private String eleveMatricule;
    private Long niveauId;
    private String niveauNom;
    private Long enseignantId;
    private String enseignantNom;
    private Integer semaine;
    private Integer annee;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateEvaluation;

    private Integer numeroLecon;
    private String themesCouverts;

    // Notes
    private Double noteLesen;
    private Double noteHoren;
    private Double noteSchreiben;
    private Double noteGrammatik;
    private Double noteSprechen;

    // Moyenne calculée (non stockée en base)
    private Double moyenne;

    private String commentaire;
    private String statut;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateSaisie;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateValidation;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dateCreation;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dateModification;
}
