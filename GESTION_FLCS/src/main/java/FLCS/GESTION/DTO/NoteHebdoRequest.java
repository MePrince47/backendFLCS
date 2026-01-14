package FLCS.GESTION.DTO;

import jakarta.validation.constraints.*;

public record NoteHebdoRequest(

    @NotNull
    Long evaluationHebdoId,

    @NotNull
    Long eleveId,

    @NotNull @PositiveOrZero
    Double les,

    @NotNull @PositiveOrZero
    Double hor,

    @NotNull @PositiveOrZero
    Double schreib,

    @NotNull @PositiveOrZero
    Double gramm,

    @NotNull @PositiveOrZero
    Double spre
) {}
