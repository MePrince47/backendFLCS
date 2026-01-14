package FLCS.GESTION.DTO;

public record ResultatResponse(
    Long id,
    String nomEleve,

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

