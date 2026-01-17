package FLCS.GESTION.DTO;

import java.util.List;

public record ResultatNiveauPDF(
    String codeNiveau,
    String rentree,
    List<ResultatElevePDF> eleves,

    Double moyenneClasse,
    String majorClasse
) {}
