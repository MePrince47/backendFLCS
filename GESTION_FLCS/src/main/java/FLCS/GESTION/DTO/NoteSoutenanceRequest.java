package FLCS.GESTION.DTO;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class NoteSoutenanceRequest {

    @NotNull
    private Long eleveId;

    @NotNull
    private Long niveauId;

    @NotNull
    @Min(0)
    @Max(20)
    private Double note;
}
