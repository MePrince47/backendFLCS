package FLCS.GESTION.DTO;

public record PaiementResumeResponse(
    Long eleveId,
    String nomEleve,
    Double montantTotal,
    Double totalPaye,
    Double resteAPayer
) {}
