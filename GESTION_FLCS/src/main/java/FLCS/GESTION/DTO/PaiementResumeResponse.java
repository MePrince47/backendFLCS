package FLCS.GESTION.DTO;

import java.time.LocalDate;

public record PaiementResumeResponse(
    Long eleveId,
    String nomEleve,
    Double montantTotal,
    Double totalPaye,
    Double resteAPayer
) {}
