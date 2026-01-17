package FLCS.GESTION.CONTROLLER;

import FLCS.GESTION.ENTITEES.EvaluationHebdo;
import FLCS.GESTION.SERVICE.EvaluationHebdoService;

import FLCS.GESTION.DTO.NoteResponseDTO;
import FLCS.GESTION.DTO.EvaluationHebdoResponse;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/evaluations-hebdo")
public class EvaluationHebdoController {

    private final EvaluationHebdoService service;

    public EvaluationHebdoController(EvaluationHebdoService service) {
        this.service = service;
    }

    // Création automatique des 7 semaines pour un niveau
    @PreAuthorize("hasAnyRole('ENSEIGNANT','SECRETAIRE')")
    @PostMapping("/niveau/{niveauId}")
    public ResponseEntity<Void> creerPourNiveau(
            @PathVariable Long niveauId
    ) {
        service.creerEvaluationsPourNiveau(niveauId);
        return ResponseEntity.ok().build();
    }

    // Liste des évaluations hebdomadaires d’un niveau
    @PreAuthorize("hasAnyRole('ENSEIGNANT','SECRETAIRE', 'ADMIN')")
    @GetMapping("/niveau/{niveauId}")
    public ResponseEntity<List<EvaluationHebdoResponse>> lireParNiveau(
            @PathVariable Long niveauId
    ) {
        return ResponseEntity.ok(service.getResponsesByNiveau(niveauId));
    }

}
