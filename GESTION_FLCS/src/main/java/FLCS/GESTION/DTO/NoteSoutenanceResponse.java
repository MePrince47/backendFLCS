package FLCS.GESTION.DTO;

public record NoteSoutenanceResponse(
    Long id,
    String eleve,
    String niveau,
    Double note
) {}
