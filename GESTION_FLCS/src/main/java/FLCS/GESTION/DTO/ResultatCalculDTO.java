package FLCS.GESTION.DTO;

/**
 * DTO représentant le résultat final d'un élève pour un niveau donné.
 *
 * - A1 / A2 : moyenne générale uniquement
 * - B1 / B2 : résultats par module
 * - Soutenance :
 *      - A2 : prise en compte dans la moyenne
 *      - B2 : affichée uniquement
 */
public record ResultatCalculDTO(

    Long eleveId,
    Long niveauId,

    // ===== Résultats par module (B1 / B2 uniquement) =====
    Double moyLes,
    Double moyHor,
    Double moySchreib,
    Double moyGramm,
    Double moySpre,

    // ===== Notes Endprüfung par module (B1 / B2) =====
    Double endLes,
    Double endHor,
    Double endSchreib,
    Double endGramm,
    Double endSpre,

    // ===== Soutenance (sur 20) =====
    // - utilisée pour A2
    // - affichée seulement pour B2
    Double noteSoutenance,

    // ===== Moyenne générale (A1 / A2 uniquement) =====
    Double moyenneGenerale,

    // ===== Résultat final =====
    boolean admis

) {}
