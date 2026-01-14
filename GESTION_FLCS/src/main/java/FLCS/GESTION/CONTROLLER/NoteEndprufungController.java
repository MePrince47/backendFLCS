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

import java.util.List;


@RestController
@RequestMapping("/api/notes-endprufung")
public class NoteEndprufungController {

    private final NoteEndprufungService service;

    public NoteEndprufungController(NoteEndprufungService service) {
        this.service = service;
    }

    // Saisir une note Endprufung
    @PreAuthorize("hasAnyRole('ENSEIGNANT','SECRETAIRE')")
    @PostMapping
    public ResponseEntity<NoteResponse> creer(
            @Valid @RequestBody NoteEndprufungRequest request
    ) {
        return ResponseEntity.ok(service.creer(request));
    }

    // Modifier une note Endprufung
    @PreAuthorize("hasRole('ENSEIGNANT')")
    @PutMapping("/{noteId}")
    public ResponseEntity<NoteResponse> modifier(
            @PathVariable Long noteId,
            @Valid @RequestBody NoteEndprufungRequest request
    ) {
        return ResponseEntity.ok(service.modifier(noteId, request));
    }

    // Résultats finaux d’un niveau
    @PreAuthorize("hasAnyRole('ENSEIGNANT','SECRETAIRE', 'ADMIN')")
    @GetMapping("/niveau/{niveauId}")
    public ResponseEntity<List<NoteResponse>> lireParNiveau(
            @PathVariable Long niveauId
    ) {
        return ResponseEntity.ok(service.lireParNiveau(niveauId));
    }

    // Résultat final d’un élève pour un niveau
    @PreAuthorize("hasAnyRole('ENSEIGNANT','SECRETAIRE', 'ADMIN')")
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
