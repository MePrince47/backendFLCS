package FLCS.GESTION.CONTROLLER;

import FLCS.GESTION.SERVICE.NoteHebdoService;
import FLCS.GESTION.DTO.NoteResponse;
import FLCS.GESTION.DTO.NoteHebdoRequest;

import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;

@Tag(
    name = "Notes – Évaluations hebdomadaires",
    description = "Saisie et consultation des notes hebdomadaires"
)
@RestController
@RequestMapping("/api/notes-hebdo")
public class NoteHebdoController {

    private final NoteHebdoService service;

    public NoteHebdoController(NoteHebdoService service) {
        this.service = service;
    }

    // Saisir
    @Operation(
        summary = "Saisir une note hebdomadaire",
        description = "Permet de saisir la note d’un élève pour une semaine donnée"
    )
    @ApiResponse(responseCode = "200", description = "Note enregistrée")
    @PreAuthorize("hasAnyRole('ADMIN','SECRETAIRE')")
    @PostMapping
    public ResponseEntity<NoteResponse> creer(
            @Valid @RequestBody NoteHebdoRequest request
    ) {
        return ResponseEntity.ok(service.saisir(request));
    }

    // Modifier
    @Operation(
        summary = "Modifier une note hebdomadaire",
        description = "Modifie la note hebdomadaire d’un élève pour une évaluation donnée"
    )
    @ApiResponse(responseCode = "200", description = "Note modifiée")
    @PreAuthorize("hasAnyRole('ADMIN','SECRETAIRE')")
    @PutMapping("/eleve/{eleveId}/evaluation/{evaluationHebdoId}")
    public ResponseEntity<NoteResponse> modifier(
            @PathVariable Long eleveId,
            @PathVariable Long evaluationHebdoId,
            @Valid @RequestBody NoteHebdoRequest request
    ) {
        return ResponseEntity.ok(
            service.modifierParEleveEtEvaluation(
                eleveId,
                evaluationHebdoId,
                request
            )
        );
    }


    // Toutes les notes d’un niveau
    @Operation(
        summary = "Notes hebdomadaires d’un niveau",
        description = "Retourne toutes les notes hebdomadaires d’un niveau"
    )
    @ApiResponse(responseCode = "200", description = "Liste des notes")
    @PreAuthorize("hasAnyRole('ENSEIGNANT','SECRETAIRE','ADMIN')")
    @GetMapping("/niveau/{niveauId}")
    public ResponseEntity<List<NoteResponse>> lireParNiveau(
            @PathVariable Long niveauId
    ) {
        return ResponseEntity.ok(service.lireParNiveau(niveauId));
    }

    // Notes d’un niveau pour une semaine
    @Operation(
        summary = "Notes hebdomadaires par semaine",
        description = "Retourne les notes d’un niveau pour une semaine précise"
    )
    @ApiResponse(responseCode = "200", description = "Notes de la semaine")
    @PreAuthorize("hasAnyRole('ENSEIGNANT','SECRETAIRE','ADMIN')")
    @GetMapping("/niveau/{niveauId}/semaine/{semaine}")
    public ResponseEntity<List<NoteResponse>> lireParNiveauEtSemaine(
            @PathVariable Long niveauId,
            @PathVariable Integer semaine
    ) {
        return ResponseEntity.ok(
                service.lireParNiveauEtSemaine(niveauId, semaine)
        );
    }

    // Notes d’un élève pour un niveau
    @Operation(
        summary = "Notes d’un élève",
        description = "Retourne toutes les notes hebdomadaires d’un élève pour un niveau"
    )
    @ApiResponse(responseCode = "200", description = "Notes de l’élève")
    @PreAuthorize("hasAnyRole('ENSEIGNANT','SECRETAIRE','ADMIN')")
    @GetMapping("/eleve/{eleveId}/niveau/{niveauId}")
    public ResponseEntity<List<NoteResponse>> lireParEleveEtNiveau(
            @PathVariable Long eleveId,
            @PathVariable Long niveauId
    ) {
        return ResponseEntity.ok(
                service.lireParEleveEtNiveau(eleveId, niveauId)
        );
    }
}
