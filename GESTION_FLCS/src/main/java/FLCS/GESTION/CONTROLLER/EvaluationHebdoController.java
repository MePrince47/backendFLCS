package FLCS.GESTION.CONTROLLER;

import FLCS.GESTION.ENTITEES.EvaluationHebdo;
import FLCS.GESTION.SERVICE.EvaluationHebdoService;

import FLCS.GESTION.DTO.NoteResponseDTO;
import FLCS.GESTION.DTO.EvaluationHebdoResponse;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;

@Tag(
    name = "Évaluations hebdomadaires",
    description = "Gestion des évaluations hebdomadaires par niveau"
)
@RestController
@RequestMapping("/api/evaluations-hebdo")
public class EvaluationHebdoController {

    private final EvaluationHebdoService service;

    public EvaluationHebdoController(EvaluationHebdoService service) {
        this.service = service;
    }

    // Création automatique des 7 semaines pour un niveau
    @Operation(
        summary = "Créer les évaluations hebdomadaires",
        description = "Génère automatiquement les 7 semaines d’évaluations pour un niveau lors de la creation d'une rentrée"
    )
    @ApiResponse(responseCode = "200", description = "Évaluations créées")
    @PreAuthorize("hasAnyRole('ENSEIGNANT','SECRETAIRE')")
    @PostMapping("/niveau/{niveauId}")
    public ResponseEntity<Void> creerPourNiveau(
            @PathVariable Long niveauId
    ) {
        service.creerEvaluationsPourNiveau(niveauId);
        return ResponseEntity.ok().build();
    }

    // Liste des évaluations hebdomadaires d’un niveau
    @Operation(
        summary = "Lister les évaluations hebdomadaires",
        description = "Retourne toutes les évaluations hebdomadaires d’un niveau"
    )
    @ApiResponse(responseCode = "200", description = "Liste des évaluations")
    @PreAuthorize("hasAnyRole('ENSEIGNANT','SECRETAIRE','ADMIN')")
    @GetMapping("/niveau/{niveauId}")
    public ResponseEntity<List<EvaluationHebdoResponse>> lireParNiveau(
            @PathVariable Long niveauId
    ) {
        return ResponseEntity.ok(service.getResponsesByNiveau(niveauId));
    }

}
