package FLCS.GESTION.Mappers;

import java.util.List;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import FLCS.GESTION.Dtos.Request.RentreeRequest;
import FLCS.GESTION.Dtos.response.RentreeResponse;
import FLCS.GESTION.Entitees.Niveau;
import FLCS.GESTION.Entitees.Rentree;

public abstract class RentreeMapper {

    @Autowired
    protected Niveau niveauMapper;

    // Request → Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "niveaux", ignore = true)
    @Mapping(target = "eleves", ignore = true)
    @Mapping(target = "nombrePlacesPrises", constant = "0")
    @Mapping(target = "statut", expression = "java(com.flcs.entity.Rentree.Statut.PLANIFIEE)")
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "dateModification", ignore = true)
    @Mapping(target = "version", ignore = true)
    public abstract Rentree toEntity(RentreeRequest request);

    // Entity → Response
    @Mapping(target = "nombreNiveaux", expression = "java(rentree.getNiveaux() != null ? (long) rentree.getNiveaux().size() : 0L)")
    @Mapping(target = "nombreEleves", expression = "java(rentree.getEleves() != null ? (long) rentree.getEleves().size() : 0L)")
    @Mapping(target = "statut", source = "statut", qualifiedByName = "statutToString")
    public abstract RentreeResponse toResponse(Rentree rentree);

    // List Entity → List Response
    public abstract List<RentreeResponse> toResponseList(List<Rentree> rentrees);

    // Update Entity from Request
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "niveaux", ignore = true)
    @Mapping(target = "eleves", ignore = true)
    @Mapping(target = "nombrePlacesPrises", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "dateModification", ignore = true)
    @Mapping(target = "version", ignore = true)
    public abstract void updateEntity(@MappingTarget Rentree rentree, RentreeRequest request);

    // Helper methods
    @Named("statutToString")
    public String statutToString(FLCS.GESTION.Entitees.Rentree.Statut statut) {
        return statut != null ? statut.name() : null;
    }

    @Named("stringToStatut")
    public FLCS.GESTION.Entitees.Rentree.Statut stringToStatut(String statut) {
        if (statut == null)
            return null;
        try {
            return FLCS.GESTION.Entitees.Rentree.Statut.valueOf(statut.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    // Custom mapping pour calculer le statut selon les dates
    @AfterMapping
    protected void calculateStatut(@MappingTarget Rentree rentree) {
        if (rentree.getDateDebut() != null && rentree.getDateFin() != null) {
            java.time.LocalDate aujourdhui = java.time.LocalDate.now();
            if (aujourdhui.isBefore(rentree.getDateDebut())) {
                rentree.setStatut(FLCS.GESTION.Entitees.Rentree.Statut.PLANIFIEE);
            } else if (aujourdhui.isAfter(rentree.getDateFin())) {
                rentree.setStatut(FLCS.GESTION.Entitees.Rentree.Statut.TERMINEE);
            } else {
                rentree.setStatut(FLCS.GESTION.Entitees.Rentree.Statut.EN_COURS);
            }
        }
    }

}
