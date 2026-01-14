package FLCS.GESTION.DTO;

import java.time.LocalDate;
import java.util.List;

public record EndprufungResponse(
    Long id,
    LocalDate dateExamen,
    List<NoteResponseDTO> notes
) {}
