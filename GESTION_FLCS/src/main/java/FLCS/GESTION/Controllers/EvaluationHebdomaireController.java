package FLCS.GESTION.Controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import FLCS.GESTION.Dtos.Request.EvaluationHebdomadaireRequest;
import FLCS.GESTION.Dtos.response.ApiResponse;
import FLCS.GESTION.Dtos.response.EvaluationHebdomadaireResponse;
import FLCS.GESTION.Entitees.EvaluationHebdomadaire;
import FLCS.GESTION.Services.EvaluationHebdomadaireService;
import FLCS.GESTION.Services.NiveauService;
import jakarta.validation.Valid;

public class EvaluationHebdomaireController {
    private final EvaluationHebdomadaireService evaluationService;

    public EvaluationHebdomaireController(EvaluationHebdomadaireService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EvaluationHebdomadaireResponse>> creerEvaluation(
            @Valid @RequestBody EvaluationHebdomadaireRequest request) {
        EvaluationHebdomadaireResponse response = evaluationService.creerEvaluation(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Évaluation créée avec succès", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EvaluationHebdomadaireResponse>> mettreAJourEvaluation(
            @PathVariable Long id,
            @Valid @RequestBody EvaluationHebdomadaireRequest request) {
        EvaluationHebdomadaireResponse response = evaluationService.mettreAJourEvaluation(id, request);
        return ResponseEntity.ok(ApiResponse.success("Évaluation mise à jour avec succès", response));
    }

    @PostMapping("/{id}/valider")
    public ResponseEntity<ApiResponse<EvaluationHebdomadaireResponse>> validerEvaluation(@PathVariable Long id) {
        EvaluationHebdomadaireResponse response = evaluationService.validerEvaluation(id);
        return ResponseEntity.ok(ApiResponse.success("Évaluation validée avec succès", response));
    }

    @PostMapping("/{id}/annuler")
    public ResponseEntity<ApiResponse<Void>> annulerEvaluation(@PathVariable Long id) {
        evaluationService.annulerEvaluation(id);
        return ResponseEntity.ok(ApiResponse.success("Évaluation annulée avec succès", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> supprimerEvaluation(@PathVariable Long id) {
        evaluationService.supprimerEvaluation(id);
        return ResponseEntity.ok(ApiResponse.success("Évaluation supprimée avec succès", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EvaluationHebdomadaireResponse>> getEvaluation(@PathVariable Long id) {
        EvaluationHebdomadaireResponse response = evaluationService.getEvaluationById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/eleve/{eleveId}")
    public ResponseEntity<ApiResponse<List<EvaluationHebdomadaireResponse>>> getEvaluationsParEleve(
            @PathVariable Long eleveId) {
        List<EvaluationHebdomadaireResponse> responses = evaluationService.getEvaluationsByEleve(eleveId);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping("/niveau/{niveauId}")
    public ResponseEntity<ApiResponse<List<EvaluationHebdomadaireResponse>>> getEvaluationsParNiveau(
            @PathVariable Long niveauId) {
        List<EvaluationHebdomadaireResponse> responses = evaluationService.getEvaluationsByNiveau(niveauId);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping("/enseignant/{enseignantId}")
    public ResponseEntity<ApiResponse<List<EvaluationHebdomadaireResponse>>> getEvaluationsParEnseignant(
            @PathVariable Long enseignantId) {
        List<EvaluationHebdomadaireResponse> responses = evaluationService.getEvaluationsByEnseignant(enseignantId);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping("/semaine")
    public ResponseEntity<ApiResponse<List<EvaluationHebdomadaireResponse>>> getEvaluationsParSemaine(
            @RequestParam Integer semaine,
            @RequestParam Integer annee) {
        List<EvaluationHebdomadaireResponse> responses = evaluationService.getEvaluationsBySemaine(semaine, annee);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping("/classe-semaine")
    public ResponseEntity<ApiResponse<List<EvaluationHebdomadaireResponse>>> getEvaluationsParClasseEtSemaine(
            @RequestParam Long niveauId,
            @RequestParam Integer semaine,
            @RequestParam Integer annee) {
        List<EvaluationHebdomadaireResponse> responses = evaluationService.getEvaluationsByNiveauEtSemaine(
                niveauId, semaine, annee);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping("/moyenne-globale")
    public ResponseEntity<ApiResponse<Double>> getMoyenneGlobale(
            @RequestParam Long eleveId,
            @RequestParam Long niveauId) {
        Double moyenne = evaluationService.calculerMoyenneHebdoGlobale(eleveId, niveauId);
        return ResponseEntity.ok(ApiResponse.success("Moyenne hebdomadaire globale", moyenne));
    }

    @GetMapping("/verifier-existence")
    public ResponseEntity<ApiResponse<Boolean>> verifierExistence(
            @RequestParam Long eleveId,
            @RequestParam Long niveauId,
            @RequestParam Integer semaine,
            @RequestParam Integer annee) {
        boolean existe = evaluationService.existeEvaluationPourSemaine(eleveId, niveauId, semaine, annee);
        return ResponseEntity.ok(ApiResponse.success(existe));
    }

    @GetMapping("/non-remplies")
    public ResponseEntity<ApiResponse<List<EvaluationHebdomadaireResponse>>> getEvaluationsNonRemplies(
            @RequestParam Long niveauId,
            @RequestParam Integer semaine,
            @RequestParam Integer annee) {
        List<EvaluationHebdomadaireResponse> responses = evaluationService.getEvaluationsNonRemplies(
                niveauId, semaine, annee);
        return ResponseEntity.ok(ApiResponse.success("Évaluations non remplies", responses));
    }

}
