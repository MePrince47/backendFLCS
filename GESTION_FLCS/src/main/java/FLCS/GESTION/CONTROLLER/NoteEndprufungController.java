package FLCS.GESTION.CONTROLLER;

import FLCS.GESTION.ENTITEES.NoteEndprufung;
import FLCS.GESTION.SERVICE.NoteEndprufungService;
import FLCS.GESTION.DTO.NoteResponse;
import FLCS.GESTION.DTO.NoteEndprufungRequest;

import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;


@Tag(
    name = "Notes – Examen final",
    description = "Gestion des notes de l’examen final (Endprüfung)"
)
@RestController
@RequestMapping("/api/notes-endprufung")
public class NoteEndprufungController {

    private final NoteEndprufungService service;

    public NoteEndprufungController(NoteEndprufungService service) {
        this.service = service;
    }

    // Saisir une note Endprufung
    @Operation(
        summary = "Saisir une note d’examen final",
        description = "Permet de saisir la note finale (Endprüfung) d’un élève"
    )
    @ApiResponse(responseCode = "200", description = "Note enregistrée avec succès")
    @PreAuthorize("hasAnyRole('ENSEIGNANT','SECRETAIRE')")
    @PostMapping
    public ResponseEntity<NoteResponse> creer(
            @Valid @RequestBody NoteEndprufungRequest request
    ) {
        return ResponseEntity.ok(service.creer(request));
    }

    // Modifier une note Endprufung
    @Operation(
        summary = "Modifier une note d’examen final",
        description = "Permet de corriger ou ajuster une note finale existante"
    )
    @ApiResponse(responseCode = "200", description = "Note modifiée avec succès")
    @PreAuthorize("hasRole('ENSEIGNANT')")
    @PutMapping("/{noteId}")
    public ResponseEntity<NoteResponse> modifier(
            @PathVariable Long noteId,
            @Valid @RequestBody NoteEndprufungRequest request
    ) {
        return ResponseEntity.ok(service.modifier(noteId, request));
    }

    // Résultats finaux d’un niveau
    @Operation(
        summary = "Résultats finaux d’un niveau",
        description = "Retourne toutes les notes finales des élèves d’un niveau"
    )
    @ApiResponse(responseCode = "200", description = "Liste des notes finales")
    @PreAuthorize("hasAnyRole('ENSEIGNANT','SECRETAIRE','ADMIN')")
    @GetMapping("/niveau/{niveauId}")
    public ResponseEntity<List<NoteResponse>> lireParNiveau(
            @PathVariable Long niveauId
    ) {
        return ResponseEntity.ok(service.lireParNiveau(niveauId));
    }

    // Résultat final d’un élève pour un niveau
    @Operation(
        summary = "Résultat final d’un élève",
        description = "Retourne la note finale d’un élève pour un niveau donné"
    )
    @ApiResponse(responseCode = "200", description = "Note finale trouvée")
    @ApiResponse(responseCode = "404", description = "Note inexistante")
    @PreAuthorize("hasAnyRole('ENSEIGNANT','SECRETAIRE','ADMIN')")
    @GetMapping("/eleve/{eleveId}/niveau/{niveauId}")
    public ResponseEntity<NoteResponse> lireParEleveEtNiveau(
            @PathVariable Long eleveId,
            @PathVariable Long niveauId
    ) {
        return ResponseEntity.ok(
                service.lireParEleveEtNiveau(eleveId, niveauId)
        );
    }
}
