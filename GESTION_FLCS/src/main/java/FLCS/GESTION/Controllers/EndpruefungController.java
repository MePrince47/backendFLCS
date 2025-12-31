package FLCS.GESTION.Controllers;

import java.util.List;

import javax.management.relation.RelationNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import FLCS.GESTION.Dtos.Request.EndpruefungRequest;
import FLCS.GESTION.Dtos.response.ApiResponse;
import FLCS.GESTION.Dtos.response.EndpruefungResponse;
import FLCS.GESTION.Services.EndpruefungService;
import FLCS.GESTION.Services.EndpruefungService;
import jakarta.validation.Valid;

public class EndpruefungController {
     private final EndpruefungService endpruefungService;

    public EndpruefungController(EndpruefungService endpruefungService) {
        this.endpruefungService = endpruefungService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EndpruefungResponse>> creerEndpruefung(
            @Valid @RequestBody EndpruefungRequest request) {
        EndpruefungResponse response = endpruefungService.creerEndpruefung(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Évaluation créée avec succès", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EndpruefungResponse>> mettreAJourEvaluation(
            @PathVariable Long id,
            @Valid @RequestBody EndpruefungRequest request) throws RelationNotFoundException {
        EndpruefungResponse response = endpruefungService.mettreAJourEndpruefung(id, request);
        return ResponseEntity.ok(ApiResponse.success("Évaluation mise à jour avec succès", response));
    }

    @PostMapping("/{id}/valider")
    public ResponseEntity<ApiResponse<EndpruefungResponse>> validerEvaluation(@PathVariable Long id) {
        EndpruefungResponse response = endpruefungService.validerEndpruefung(id);
        return ResponseEntity.ok(ApiResponse.success("Évaluation validée avec succès", response));
    }

    @PostMapping("/{id}/annuler")
    public ResponseEntity<ApiResponse<Void>> annulerEvaluation(@PathVariable Long id) {
        endpruefungService.annulerEndpruefung(id);
        return ResponseEntity.ok(ApiResponse.success("Évaluation annulée avec succès", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> supprimerEvaluation(@PathVariable Long id) {
        endpruefungService.supprimerEndpruefung(id);
        return ResponseEntity.ok(ApiResponse.success("Évaluation supprimée avec succès", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EndpruefungResponse>> getEvaluation(@PathVariable Long id) {
        EndpruefungResponse response = endpruefungService.getEndpruefungById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/eleve/{eleveId}")
    public ResponseEntity<ApiResponse<List<EndpruefungResponse>>> getEvaluationsParEleve(
            @PathVariable Long eleveId) {
        List<EndpruefungResponse> responses = endpruefungService.getEndpruefungenByEleve(eleveId);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping("/niveau/{niveauId}")
    public ResponseEntity<ApiResponse<List<EndpruefungResponse>>> getEvaluationsParNiveau(
            @PathVariable Long niveauId) {
        List<EndpruefungResponse> responses = endpruefungService.getEndpruefungenByNiveau(niveauId);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping("/moyenne-globale")
    public ResponseEntity<ApiResponse<Double>> getMoyenneGlobale(
            @RequestParam Long eleveId,
            @RequestParam Long niveauId) {
        Double moyenne = endpruefungService.calculerMoyenneFinale(eleveId, niveauId);
        return ResponseEntity.ok(ApiResponse.success("Moyenne hebdomadaire globale", moyenne));
    }


    @GetMapping("/non-remplies")
    public ResponseEntity<ApiResponse<List<EndpruefungResponse>>> getEvaluationsNonRemplies(
            @RequestParam Long niveauId,
            @RequestParam Integer semaine,
            @RequestParam Integer annee) {
        List<EndpruefungResponse> responses = endpruefungService.getEndpruefungenNonRemplies(
                niveauId, semaine, annee);
        return ResponseEntity.ok(ApiResponse.success("Évaluations non remplies", responses));
    }

}
