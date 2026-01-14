package FLCS.GESTION.DTO;

public record ResultatCalculDTO(
    Long eleveId,
    Long niveauId,

    Double moyLes,
    Double moyHor,
    Double moySchreib,
    Double moyGramm,
    Double moySpre,

    Double endLes,
    Double endHor,
    Double endSchreib,
    Double endGramm,
    Double endSpre,

    Double moyenneGenerale,
    boolean admis
) {}

