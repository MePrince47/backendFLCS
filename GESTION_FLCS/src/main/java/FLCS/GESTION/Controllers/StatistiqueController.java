package FLCS.GESTION.Controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import FLCS.GESTION.Dtos.response.EndpruefungResponse;
import FLCS.GESTION.Dtos.response.NiveauResponse;
import FLCS.GESTION.Entitees.Eleve;
import FLCS.GESTION.Services.EleveService;
import FLCS.GESTION.Services.EndpruefungService;
import FLCS.GESTION.Services.NiveauService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/statistiques")
public class StatistiqueController {

    private final NiveauService niveauService;
    private final EleveService eleveService;
    private final EndpruefungService endpruefungService;

    @GetMapping("/reussite-par-niveau")
    public ResponseEntity<Map<String, Double>> getTauxReussiteParNiveau() {
        Map<String, Double> resultats = new HashMap<>();

        // Récupère tous les niveaux via le service (DTOs)
        List<NiveauResponse> niveaux = niveauService.getAllNiveaux();
        for (NiveauResponse niveau : niveaux) {
            List<Eleve> elevesDuNiveau = eleveService.findByNiveau(niveau.getId());

            int totalEleves = elevesDuNiveau.size();
            int reussis = 0;

            for (Eleve eleve : elevesDuNiveau) {
                Double moyenneFinale = calculerMoyenneFinale(eleve.getId(), niveau.getId());
                if (moyenneFinale != null && moyenneFinale >= 10.0) { // Seuil de réussite
                    reussis++;
                }
            }

            double tauxReussite = totalEleves > 0 ? (reussis * 100.0 / totalEleves) : 0.0;
            resultats.put(niveau.getNom(), tauxReussite);
        }

        return ResponseEntity.ok(resultats);
    }

    /**
     * Calcule la moyenne finale pour un élève sur un niveau donné.
     * Utilise le service `EndpruefungService` pour récupérer l'évaluation finale.
     */
    private Double calculerMoyenneFinale(Long eleveId, Long niveauId) {
        EndpruefungResponse endpruefung = endpruefungService.getEndpruefungByEleveAndNiveau(eleveId,
                niveauId);
        return endpruefung != null ? endpruefung.getMoyenne() : null;
    }

}