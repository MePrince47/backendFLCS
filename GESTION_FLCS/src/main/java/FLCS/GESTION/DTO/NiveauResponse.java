package FLCS.GESTION.DTO;

import java.util.List;

public record NiveauResponse(
    Long id,
    String code,
    Integer bareme,
    List<EvaluationHebdoResponse> evaluations
    
) {}
