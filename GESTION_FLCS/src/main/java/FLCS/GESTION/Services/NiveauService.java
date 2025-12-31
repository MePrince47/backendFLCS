package FLCS.GESTION.Services;

import java.util.List;

import FLCS.GESTION.Dtos.Request.NiveauRequest;
import FLCS.GESTION.Dtos.response.NiveauResponse;
import FLCS.GESTION.Models.Niveau;
import jakarta.validation.Valid;

public interface NiveauService {

    NiveauResponse createNiveau(NiveauResponse niveauResponse);

    NiveauResponse updateNiveau(Long id, NiveauResponse niveauResponse);

    void deleteNiveau(Long id);

    NiveauResponse getNiveauById(Long id);

    List<NiveauResponse> getAllNiveaux();

    List<NiveauResponse> getNiveauxByRentree(Long rentreeId);

    List<NiveauResponse> getNiveauxByEnseignant(Long enseignantId);

    NiveauResponse assignerEnseignant(Long niveauId, Long enseignantId);

    NiveauResponse retirerEnseignant(Long niveauId);

    Long countElevesByNiveau(Long niveauId);

    boolean estComplet(Long niveauId);

    NiveauResponse updateNiveau(Long id, NiveauRequest request);

    static List<Niveau> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

}
