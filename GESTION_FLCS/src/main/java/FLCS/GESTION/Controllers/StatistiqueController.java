package FLCS.GESTION.Controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import FLCS.GESTION.Models.Eleve;
import FLCS.GESTION.Models.Niveau;
import FLCS.GESTION.Services.EleveService;
import FLCS.GESTION.Services.NiveauService;

// @RestController
@RequestMapping("/api/statistiques")
public class StatistiqueController {

    @GetMapping("/reussite-par-niveau")
    public ResponseEntity<Map<String, Double>> getTauxReussiteParNiveau() {
        Map<String, Double> resultats = new HashMap<>();

        // Pour chaque niveau (A1, A2, B1, B2)

        List<Niveau> niveaux = NiveauService.findAll();
        for (Niveau niveau : niveaux) {
            List<Eleve> elevesDuNiveau = EleveService.findByNiveau(niveau.getId());

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

    private Double calculerMoyenneFinale(Long id, Long id2) {
        return calculerMoyenneFinale(id, id2);
    }

}