package FLCS.GESTION.Services;

import FLCS.GESTION.Dtos.Request.RentreeRequest;
import FLCS.GESTION.Dtos.response.RentreeResponse;


import java.util.List;

public interface RentreeService {
    RentreeResponse createRentree(RentreeRequest request);
    RentreeResponse updateRentree(Long id, RentreeRequest request);
    void deleteRentree(Long id);
    RentreeResponse getRentreeById(Long id);
    RentreeResponse getRentreeByCode(String code);
    List<RentreeResponse> getAllRentrees();
    List<RentreeResponse> getRentreesByStatut(String statut);
    void creerNiveauxPourRentree(Long rentreeId);
 Long countRentrees();
    
    List<RentreeResponse> getRentreesEnCours();
}
