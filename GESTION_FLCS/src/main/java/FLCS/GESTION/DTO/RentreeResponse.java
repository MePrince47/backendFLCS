package FLCS.GESTION.DTO;

import java.time.LocalDate;
import java.util.List;

public record RentreeResponse(
    Long id,
    String nomRentree,
    LocalDate dateDebut,
    List<NiveauResponse> niveaux
) {}
