package FLCS.GESTION.DTO;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record EndprufungRequest(
    @NotNull Long niveauId,
    @NotNull LocalDate dateExam
) {}
