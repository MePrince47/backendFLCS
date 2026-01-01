// service/EndpruefungService.java
package FLCS.GESTION.Services;

import FLCS.GESTION.Dtos.Request.EndpruefungRequest;
import FLCS.GESTION.Dtos.response.EndpruefungResponse;
import FLCS.GESTION.Dtos.response.EvaluationHebdomadaireResponse;

import java.util.List;

public interface EndpruefungService {

    EndpruefungResponse creerEndpruefung(EndpruefungRequest request);

    EndpruefungResponse mettreAJourEndpruefung(Long id, EndpruefungRequest request);

    EndpruefungResponse validerEndpruefung(Long id);

    EndpruefungResponse corrigerEndpruefung(Long id, EndpruefungRequest request);

    void annulerEndpruefung(Long id);

    void supprimerEndpruefung(Long id);

    EndpruefungResponse getEndpruefungById(Long id);

    List<EndpruefungResponse> getEndpruefungenByEleve(Long eleveId);

    List<EndpruefungResponse> getEndpruefungenByNiveau(Long niveauId);

    EndpruefungResponse getEndpruefungByEleveAndNiveau(Long eleveId, Long niveauId);

    List<EndpruefungResponse> getEndpruefungenByResultat(String resultat);

    List<EndpruefungResponse> getEndpruefungensManquantes(Long niveauId, Integer semaine, Integer annee);

    List<EndpruefungResponse> getEndpruefungenNonRemplies(Long niveauId, Integer semaine, Integer annee);

    double calculerMoyenneFinale(Long endpruefungId, Long niveauId);

    EndpruefungResponse proclamerResultat(Long id);
}