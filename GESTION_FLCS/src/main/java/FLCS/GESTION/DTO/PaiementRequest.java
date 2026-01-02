package FLCS.GESTION.DTO;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record PaiementRequest(
    @NotBlank Double montant,
    @NotNull LocalDate datePaiement,
    @NotNull String referenceVirement,
    @NotNull Long eleveId
) {}
