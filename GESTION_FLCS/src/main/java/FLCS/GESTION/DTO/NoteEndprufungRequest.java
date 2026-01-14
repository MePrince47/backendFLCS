package FLCS.GESTION.DTO;

import jakarta.validation.constraints.*;

public record NoteEndprufungRequest(

    @NotNull
    Long endprufungId,

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
