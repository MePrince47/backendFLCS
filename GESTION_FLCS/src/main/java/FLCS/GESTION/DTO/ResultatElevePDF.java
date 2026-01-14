package FLCS.GESTION.DTO;

import java.util.List;

public record ResultatElevePDF(
    Long eleveId,
    String nomComplet,

    List<Double> notesHebdo, // 7 valeurs

    Double moyenneHebdo,
    Double noteEndprufung,
    Double moyenneFinale,
    boolean admis
) {}
