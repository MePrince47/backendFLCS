package FLCS.GESTION.DTO;

import java.util.List;

public record SearchResponse<T>(

    int count,
    String message,
    List<T> data
){}
