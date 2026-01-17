package FLCS.GESTION.DTO;

import java.time.LocalDate;

public record PaiementHistoriqueResponse(
    Long id,
    Double montant,
    LocalDate datePaiement,
    String referenceVirement
) {}
