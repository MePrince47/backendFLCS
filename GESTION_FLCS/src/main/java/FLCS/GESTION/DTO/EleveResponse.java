package FLCS.GESTION.DTO;

import java.time.LocalDate;

public record EleveResponse(
    Long id,
    String nom,
    String prenom,
    LocalDate dateNaiss,
    String niveauScolaire,
    String typeProcedure,
    String telCandidat,
    String telParent,
    String statut,

    String partenaire,
    String niveauLangue,
    String rentree
) {}
