package FLCS.GESTION.DTO;

import java.time.LocalDate;

public record PaiementResponse(
    Long id,
    Double montant,
    LocalDate datePaiement,
    String referenceVirement,
    Long eleveId,
    String nomEleve
) {}
