package FLCS.GESTION.Dtos.response;

import lombok.Data;
import java.time.LocalDate;

import FLCS.GESTION.Entitees.BaseEntity;
import FLCS.GESTION.Entitees.Rentree;

public class RentreeResponse {

    private Long id;
    private String uuid;
    private String code;
    private String nom;
    private String description;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Integer nombrePlacesMax;
    private Integer nombrePlacesPrises;
    private String statut;
    private static Long nombreNiveaux;
    private Long nombreEleves;

    public static RentreeResponse fromEntity(Rentree rentree, Long nombreNiveaux2, Long nombreEleves2) {

        RentreeResponse response = new RentreeResponse();
        // response.Id(rentree.getId());
        return response;

    }

}
