package FLCS.GESTION.CONTROLLER;

import FLCS.GESTION.DTO.*;
import FLCS.GESTION.SERVICE.NoteSoutenanceService;

import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.parameters.RequestBody;


@Tag(
    name = "Soutenances",
    description = "Gestion des notes de soutenance des élèves"
)
@RestController
@RequestMapping("/api/soutenances")
public class NoteSoutenanceController {

    private final NoteSoutenanceService service;

    public NoteSoutenanceController(NoteSoutenanceService service) {
        this.service = service;
    }

    /**
     * Attribuer une note de soutenance à un élève
     */
    @Operation(
        summary = "Attribuer une note de soutenance",
        description = """
            Permet d’enregistrer la note de soutenance d’un élève
            pour un niveau donné.
            
            📌 Règles :
            - Une seule note par élève et par niveau
            - Note sur 20
            - Réservé à l’administration
            """
    )
    @ApiResponse(responseCode = "201", description = "Note enregistrée avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @ApiResponse(responseCode = "409", description = "Note déjà existante")
    @PreAuthorize("hasAnyRole('ADMIN','SECRETAIRE')")
    @PostMapping
    public ResponseEntity<NoteSoutenanceResponse> attribuer(
        @Valid @RequestBody NoteSoutenanceRequest request
    ) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(service.attribuer(request));
    }

    /**
     * Consulter la note de soutenance d’un élève
     */
    @Operation(
        summary = "Consulter une note de soutenance",
        description = """
            Récupère la note de soutenance d’un élève
            pour un niveau donné.
            """
    )
    @ApiResponse(responseCode = "200", description = "Note trouvée")
    @ApiResponse(responseCode = "404", description = "Note inexistante")
    @PreAuthorize("hasAnyRole('ADMIN','SECRETAIRE','ENSEIGNANT')")
    @GetMapping("/eleve/{eleveId}/niveau/{niveauId}")
    public NoteSoutenanceResponse consulter(
        @PathVariable Long eleveId,
        @PathVariable Long niveauId
    ) {
        return service.consulter(eleveId, niveauId);
    }
}
