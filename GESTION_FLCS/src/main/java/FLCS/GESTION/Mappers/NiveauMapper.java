package FLCS.GESTION.Mappers;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import FLCS.GESTION.Dtos.Request.NiveauRequest;
import FLCS.GESTION.Dtos.response.NiveauResponse;
import FLCS.GESTION.Models.Niveau;
import FLCS.GESTION.Models.Rentree;

import java.util.List;

@Component
// @Mapper(componentModel = "spring", uses = { RentreeMapper.class,
        // EnseignantMapper.class }, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class NiveauMapper {

    @Autowired
    protected RentreeMapper rentreeMapper;

    @Autowired
    protected EnseignantMapper enseignantMapper;

    // Request → Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "rentree", source = "rentreeId", qualifiedByName = "rentreeIdToEntity")
    @Mapping(target = "enseignant", source = "enseignantId", qualifiedByName = "enseignantIdToEntity")
    @Mapping(target = "eleves", ignore = true)
    @Mapping(target = "evaluations", ignore = true)
    @Mapping(target = "endpruefungen", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "dateModification", ignore = true)
    @Mapping(target = "version", ignore = true)
    public abstract Niveau toEntity(NiveauRequest request);

    // Entity → Response
    @Mapping(target = "rentreeId", source = "rentree.id")
    @Mapping(target = "rentreeNom", source = "rentree.nom")
    @Mapping(target = "enseignantId", source = "enseignant.id")
    @Mapping(target = "enseignantNom", expression = "java(getEnseignantFullName(niveau))")
    @Mapping(target = "nombreEleves", expression = "java(niveau.getEleves() != null ? (long) niveau.getEleves().size() : 0L)")
    @Mapping(target = "estComplet", expression = "java(isNiveauComplet(niveau))")
    @Mapping(target = "type", source = "type", qualifiedByName = "typeToString")
    @Mapping(target = "statut", source = "statut", qualifiedByName = "statutToString")
    public abstract NiveauResponse toResponse(Niveau niveau);

    // List Entity → List Response
    public abstract List<NiveauResponse> toResponseList(List<Niveau> niveaux);

    // Update Entity from Request
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "rentree", source = "rentreeId", qualifiedByName = "rentreeIdToEntity")
    @Mapping(target = "enseignant", source = "enseignantId", qualifiedByName = "enseignantIdToEntity")
    @Mapping(target = "eleves", ignore = true)
    @Mapping(target = "evaluations", ignore = true)
    @Mapping(target = "endpruefungen", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "dateModification", ignore = true)
    @Mapping(target = "version", ignore = true)
    public abstract void updateEntity(@MappingTarget Niveau niveau, NiveauRequest request);

    // Named methods
    @Named("rentreeIdToEntity")
    public Rentree rentreeIdToEntity(Long rentreeId) {
        if (rentreeId == null)
            return null;
        Rentree rentree = new Rentree();
        rentree.setId(rentreeId);
        return rentree;
    }

    @Named("enseignantIdToEntity")
    public FLCS.GESTION.Models.Enseignant enseignantIdToEntity(Long enseignantId) {
        if (enseignantId == null)
            return null;
        FLCS.GESTION.Models.Enseignant enseignant = new FLCS.GESTION.Models.Enseignant();
        enseignant.setId(enseignantId);
        return enseignant;
    }

    @Named("typeToString")
    public String typeToString(Niveau.TypeNiveau type) {
        return type != null ? type.name() : null;
    }

    @Named("statutToString")
    public String statutToString(Niveau.Statut statut) {
        return statut != null ? statut.name() : null;
    }

    // Helper methods
    protected String getEnseignantFullName(Niveau niveau) {
        if (niveau.getEnseignant() != null) {
            return niveau.getEnseignant().getNom() + " " + niveau.getEnseignant().getNom();
        }
        return null;
    }

    // protected boolean isNiveauComplet(Niveau niveau) {
    //     if (niveau.getEleves() == null || niveau.getCapaciteMax() == null) {
    //         return false;
    //     }
    //     return niveau.getEleves().size() >= niveau.getCapaciteMax();
    // }
}