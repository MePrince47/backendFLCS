package FLCS.GESTION.Mappers;

import org.springframework.stereotype.Component;
// import org.springframework.web.bind.annotation.Mapping;

import FLCS.GESTION.Dtos.Request.EndpruefungRequest;
import FLCS.GESTION.Entitees.Eleve;
import FLCS.GESTION.Entitees.Endpruefung;
import FLCS.GESTION.Entitees.Enseignant;
import FLCS.GESTION.Entitees.Niveau;
import java.util.List;
import org.mapstruct.*;
import org.mapstruct.Mapping;

@Component
// @Mapper(componentModel = "spring", unmappedTargetPolicy =
// ReportingPolicy.IGNORE)
public abstract class EndpruefungMapper {

    // Request → Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "eleve", source = "eleveId", qualifiedByName = "eleveIdToEntity")
    @Mapping(target = "niveau", source = "niveauId", qualifiedByName = "niveauIdToEntity")
    @Mapping(target = "observateur", source = "observateurId", qualifiedByName = "enseignantIdToEntity")
    @Mapping(target = "moyenneExamen", ignore = true) // Calculé automatiquement
    @Mapping(target = "moyenneFinale", ignore = true) // Calculé automatiquement
    @Mapping(target = "moyenneHebdomadaireGlobale", ignore = true) // À définir après
    @Mapping(target = "resultat", expression = "java(com.flcs.entity.Endpruefung.Resultat.ABSENT)")
    @Mapping(target = "mention", ignore = true) // Calculé automatiquement
    @Mapping(target = "statut", expression = "java(com.flcs.entity.Endpruefung.StatutEndpruefung.PLANIFIE)")
    @Mapping(target = "dateCorrection", ignore = true)
    @Mapping(target = "dateProclamation", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "dateModification", ignore = true)
    @Mapping(target = "version", ignore = true)
    public abstract Endpruefung toEntity(EndpruefungRequest request);

    // Entity → Response
    @Mapping(target = "eleveId", source = "eleve.id")
    @Mapping(target = "eleveNom", expression = "java(getEleveFullName(endpruefung))")
    @Mapping(target = "eleveMatricule", source = "eleve.matricule")
    @Mapping(target = "niveauId", source = "niveau.id")
    @Mapping(target = "niveauNom", source = "niveau.nom")
    @Mapping(target = "observateurId", source = "observateur.id")
    @Mapping(target = "observateurNom", expression = "java(getObservateurFullName(endpruefung))")
    @Mapping(target = "resultat", source = "resultat", qualifiedByName = "resultatToString")
    @Mapping(target = "mention", source = "mention", qualifiedByName = "mentionToString")
    @Mapping(target = "statut", source = "statut", qualifiedByName = "statutToString")
    @Mapping(target = "estAdmis", expression = "java(isAdmis(endpruefung))")
    public abstract Endpruefung toResponse(Endpruefung endpruefung);

    // List Entity → List Response
    public abstract List<Endpruefung> toResponseList(List<Endpruefung> endpruefungen);

    // Update Entity from Request
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "eleve", source = "eleveId", qualifiedByName = "eleveIdToEntity")
    @Mapping(target = "niveau", source = "niveauId", qualifiedByName = "niveauIdToEntity")
    @Mapping(target = "observateur", source = "observateurId", qualifiedByName = "enseignantIdToEntity")
    @Mapping(target = "moyenneExamen", ignore = true)
    @Mapping(target = "moyenneFinale", ignore = true)
    @Mapping(target = "dateCorrection", ignore = true)
    @Mapping(target = "dateProclamation", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "dateModification", ignore = true)
    @Mapping(target = "version", ignore = true)
    public abstract void updateEntity(@MappingTarget Endpruefung endpruefung, EndpruefungRequest request);

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

    @Named("resultatToString")
    public String resultatToString(Endpruefung.Resultat resultat) {
        return resultat != null ? resultat.name() : null;
    }

    // Après mapping - Calcul automatique
    // @AfterMapping
    // protected void calculateMoyennes(@MappingTarget Endpruefung endpruefung) {
    // // Calculer la moyenne d'examen
    // if (((Object) endpruefung).getNoteLesen() != null &&
    // endpruefung.getNoteHoren() != null &&
    // endpruefung.getNoteSchreiben() != null && endpruefung.getNoteGrammatik() !=
    // null &&
    // endpruefung.getNoteSprechen() != null) {
    // double moyenneExam = (endpruefung.getNoteLesen() + endpruefung.getNoteHoren()
    // +
    // endpruefung.getNoteSchreiben() + endpruefung.getNoteGrammatik() +
    // endpruefung.getNoteSprechen()) / 5.0;
    // endpruefung.setMoyenneExamen(moyenneExam);

    // // Si moyenne hebdo disponible, calculer moyenne finale
    // if (endpruefung.getMoyenneHebdomadaireGlobale() != null) {
    // endpruefung.calculerMoyenneFinale();
    // }
    // }
    // }
}