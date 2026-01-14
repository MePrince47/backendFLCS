package FLCS.GESTION.CONTROLLER;

import FLCS.GESTION.ENTITEES.NoteHebdo;
import FLCS.GESTION.SERVICE.NoteHebdoService;
import FLCS.GESTION.DTO.NoteResponse;
import FLCS.GESTION.DTO.NoteHebdoRequest;

import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;



import java.util.List;

@RestController
@RequestMapping("/api/notes-hebdo")
public class NoteHebdoController {

    private final NoteHebdoService service;

    public NoteHebdoController(NoteHebdoService service) {
        this.service = service;
    }

    // Saisir
    @PreAuthorize("hasAnyRole('ENSEIGNANT','SECRETAIRE')")
    @PostMapping
    public ResponseEntity<NoteResponse> creer(
            @Valid @RequestBody NoteHebdoRequest request
    ) {
        return ResponseEntity.ok(service.saisir(request));
    }

    // Modifier
    @PreAuthorize("hasRole('ENSEIGNANT')")
    @PutMapping("/{noteId}")
    public ResponseEntity<NoteResponse> modifier(
            @PathVariable Long noteId,
            @Valid @RequestBody NoteHebdoRequest request
    ) {
        return ResponseEntity.ok(service.modifier(noteId, request));
    }

    // Toutes les notes d’un niveau
    @PreAuthorize("hasAnyRole('ENSEIGNANT','SECRETAIRE', 'ADMIN')")
    @GetMapping("/niveau/{niveauId}")
    public ResponseEntity<List<NoteResponse>> lireParNiveau(
            @PathVariable Long niveauId
    ) {
        return ResponseEntity.ok(service.lireParNiveau(niveauId));
    }

    // Notes d’un niveau pour une semaine
    @PreAuthorize("hasAnyRole('ENSEIGNANT','SECRETAIRE', 'ADMIN')")
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
    @PreAuthorize("hasAnyRole('ENSEIGNANT','SECRETAIRE', 'ADMIN')")
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
