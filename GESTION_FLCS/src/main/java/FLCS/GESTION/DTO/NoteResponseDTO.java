package FLCS.GESTION.DTO;

public record NoteResponseDTO(
    Long eleveId,
    String nomEleve,
    Double les,
    Double hor,
    Double schreib,
    Double gramm,
    Double spre
) {}
