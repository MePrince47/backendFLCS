package FLCS.GESTION.Dtos.response;

import java.time.LocalDate;

import FLCS.GESTION.Models.Niveau;
import lombok.Data;

@Data
public class NiveauResponse {
    private Long id;
    private String uuid;
    private String code;
    private String description;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Long nombreNiveaux;
    private Long nombreEleves;

    public static NiveauResponse fromEntity(Niveau niveau, Long nombreNiveaux2, Long nombreEleves2) {
        NiveauResponse response = new NiveauResponse();
        if (niveau == null)
            return response;
        response.setId(niveau.getId());
        response.setUuid(niveau.getUuid());
        response.setCode(niveau.getNom());
        response.setDescription(niveau.getType() != null ? niveau.getType().name() : null);
        response.setDateDebut(niveau.getDateDebut());
        response.setDateFin(niveau.getDateFin());
        response.setNombreNiveaux(nombreNiveaux2);
        response.setNombreEleves(nombreEleves2);
        return response;
    }

}
