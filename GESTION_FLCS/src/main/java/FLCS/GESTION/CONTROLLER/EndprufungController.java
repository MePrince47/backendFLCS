package FLCS.GESTION.CONTROLLER;

import FLCS.GESTION.ENTITEES.Endprufung;
import FLCS.GESTION.SERVICE.EndprufungService;

import FLCS.GESTION.DTO.NoteResponseDTO;
import FLCS.GESTION.DTO.EndprufungResponse; 
import FLCS.GESTION.DTO.EndprufungRequest; 

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.time.LocalDate;

@Tag(
    name = "Examens finaux (Endprüfung)",
    description = "Gestion des examens finaux par niveau"
)
@RestController
@RequestMapping("/api/endprufung")
public class EndprufungController {

    private final EndprufungService service;

    public EndprufungController(EndprufungService service) {
        this.service = service;
    }

    // Création de l’examen final
    @Operation(
        summary = "Créer un examen final",
        description = "Crée l’examen final (Endprüfung) pour un niveau donné"
    )
    @ApiResponse(responseCode = "200", description = "Examen créé avec succès")
    @PreAuthorize("hasAnyRole('ADMIN','SECRETAIRE')")
    @PostMapping
    public ResponseEntity<EndprufungResponse> creer(
            @RequestBody @Valid EndprufungRequest request
    ) {
        return ResponseEntity.ok(service.creer(request));
    }

    // Consultation par niveau
    @Operation(
        summary = "Consulter l’examen final d’un niveau",
        description = "Retourne l’examen final associé à un niveau académique"
    )
    @ApiResponse(responseCode = "200", description = "Examen trouvé")
    @ApiResponse(responseCode = "404", description = "Examen inexistant")
    @PreAuthorize("hasAnyRole('ENSEIGNANT','SECRETAIRE','ADMIN')")
    @GetMapping("/niveau/{niveauId}")
    public ResponseEntity<EndprufungResponse> lireParNiveau(
            @PathVariable Long niveauId
    ) {
        return ResponseEntity.ok(service.getByNiveau(niveauId));
    }
}

