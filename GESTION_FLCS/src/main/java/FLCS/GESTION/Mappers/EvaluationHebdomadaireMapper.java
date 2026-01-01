package FLCS.GESTION.Mappers;

import org.mapstruct.*;
import org.springframework.stereotype.Component;

import FLCS.GESTION.Dtos.Request.EvaluationHebdomadaireRequest;
import FLCS.GESTION.Entitees.Eleve;
import FLCS.GESTION.Entitees.Enseignant;
import FLCS.GESTION.Entitees.EvaluationHebdomadaire;
import FLCS.GESTION.Entitees.Niveau;

import java.util.List;

@Component
// @Mapper(componentModel = "spring", unmappedTargetPolicy =
// ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy =
// NullValuePropertyMappingStrategy.IGNORE)
public abstract class EvaluationHebdomadaireMapper {

    // Request → Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "eleve", source = "eleveId", qualifiedByName = "eleveIdToEntity")
    @Mapping(target = "niveau", source = "niveauId", qualifiedByName = "niveauIdToEntity")
    @Mapping(target = "enseignant", source = "enseignantId", qualifiedByName = "enseignantIdToEntity")
    @Mapping(target = "dateEvaluation", source = "dateEvaluation", defaultValue = "java(java.time.LocalDate.now())")
    @Mapping(target = "statut")
    @Mapping(target = "dateSaisie", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "dateValidation", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "dateModification", ignore = true)
    @Mapping(target = "version", ignore = true)
    public abstract EvaluationHebdomadaire toEntity(EvaluationHebdomadaireRequest request);

    // Entity → Response
    @Mapping(target = "eleveId", source = "eleve.id")
    @Mapping(target = "eleveNom", expression = "java(getEleveFullName(evaluation))")
    @Mapping(target = "eleveMatricule", source = "eleve.matricule")
    @Mapping(target = "niveauId", source = "niveau.id")
    @Mapping(target = "niveauNom", source = "niveau.nom")
    @Mapping(target = "enseignantId", source = "enseignant.id")
    @Mapping(target = "enseignantNom", expression = "java(getEnseignantFullName(evaluation))")
    @Mapping(target = "moyenne", expression = "java(calculerMoyenne(evaluation))")
    @Mapping(target = "statut", source = "statut", qualifiedByName = "statutToString")
    public abstract EvaluationHebdomadaireRequest toResponse(EvaluationHebdomadaire evaluation);

    // List Entity → List Response
    public abstract List<EvaluationHebdomadaire> toResponseList(List<EvaluationHebdomadaire> evaluations);

    // Update Entity from Request
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "eleve", source = "eleveId", qualifiedByName = "eleveIdToEntity")
    @Mapping(target = "niveau", source = "niveauId", qualifiedByName = "niveauIdToEntity")
    @Mapping(target = "enseignant", source = "enseignantId", qualifiedByName = "enseignantIdToEntity")
    @Mapping(target = "dateSaisie", ignore = true)
    @Mapping(target = "dateValidation", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "dateModification", ignore = true)
    @Mapping(target = "version", ignore = true)
    public abstract void updateEntity(@MappingTarget EvaluationHebdomadaire evaluation,
            EvaluationHebdomadaireRequest request);

    // Named methods
    @Named("eleveIdToEntity")
    public Eleve eleveIdToEntity(Long eleveId) {
        if (eleveId == null)
            return null;
        Eleve eleve = new Eleve();
        eleve.setId(eleveId);
        return eleve;
    }

    @Named("niveauIdToEntity")
    public Niveau niveauIdToEntity(Long niveauId) {
        if (niveauId == null)
            return null;
        Niveau niveau = new Niveau();
        niveau.setId(niveauId);
        return niveau;
    }

    @Named("enseignantIdToEntity")
    public Enseignant enseignantIdToEntity(Long enseignantId) {
        if (enseignantId == null)
            return null;
        Enseignant enseignant = new Enseignant();
        enseignant.setId(enseignantId);
        return enseignant;
    }

}