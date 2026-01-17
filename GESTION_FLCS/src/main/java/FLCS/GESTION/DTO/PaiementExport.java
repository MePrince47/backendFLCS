package FLCS.GESTION.DTO;

import java.time.LocalDate;
import java.util.List;

public record PaiementExport(
    String nomEleve,
    Double montantTotal,
    Double totalPaye,
    Double resteAPayer,
    List<PaiementHistoriqueResponse> historique
) {}
