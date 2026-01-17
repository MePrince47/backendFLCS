package FLCS.GESTION.DTO;

import java.util.List;

public record EvaluationHebdoResponse(
    Long id,
    Integer semaineNum,
    List<NoteResponseDTO> notes
) {}
