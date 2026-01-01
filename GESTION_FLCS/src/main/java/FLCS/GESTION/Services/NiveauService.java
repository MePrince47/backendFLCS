package FLCS.GESTION.Services;

import java.util.List;

import FLCS.GESTION.Dtos.Request.NiveauRequest;
import FLCS.GESTION.Dtos.response.NiveauResponse;
import FLCS.GESTION.Entitees.Niveau;
import jakarta.validation.Valid;

public interface NiveauService {

    // Création / modification / suppression
    NiveauResponse createNiveau(NiveauResponse niveauResponse);

    NiveauResponse updateNiveau(Long id, NiveauResponse niveauResponse);

    void deleteNiveau(Long id);

    // Recherches
    NiveauResponse getNiveauById(Long id);

    List<NiveauResponse> getAllNiveaux();

    List<NiveauResponse> getNiveauxByRentree(Long rentreeId);

    List<NiveauResponse> getNiveauxByEnseignant(Long enseignantId);

    // Assignation d'enseignant
    NiveauResponse assignerEnseignant(Long niveauId, Long enseignantId);

    NiveauResponse retirerEnseignant(Long niveauId);

    // Statistiques
    Long countElevesByNiveau(Long niveauId);

    boolean estComplet(Long niveauId);

}
