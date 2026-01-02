package FLCS.GESTION.DTO;

import jakarta.validation.constraints.*;

public record PartenaireRequest(

    @NotBlank String nomPartenaire
) {}
