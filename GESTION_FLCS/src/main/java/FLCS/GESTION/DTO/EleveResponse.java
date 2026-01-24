package FLCS.GESTION.DTO;

import java.time.LocalDate;

public record EleveResponse(
    Long id,
    String nom,
    String prenom,
    LocalDate dateNaiss,
    String lieuNaiss,
    String niveauScolaire,
    String typeProcedure,
    Double montantProcedure,
    String telCandidat,
    String telParent,

    String partenaire,
    String niveauLangue,
    String rentree
) {}
