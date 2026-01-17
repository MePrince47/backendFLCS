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

import java.time.LocalDate;

@RestController
@RequestMapping("/api/endprufung")
public class EndprufungController {

    private final EndprufungService service;

    public EndprufungController(EndprufungService service) {
        this.service = service;
    }

    // Création de l’examen final
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','SECRETAIRE')")
    public ResponseEntity<EndprufungResponse> creer(
            @RequestBody @Valid EndprufungRequest request
    ) {
        return ResponseEntity.ok(service.creer(request));
    }

    // Consultation par niveau
    @GetMapping("/niveau/{niveauId}")
    @PreAuthorize("hasAnyRole('ENSEIGNANT','SECRETAIRE','ADMIN')")
    public ResponseEntity<EndprufungResponse> lireParNiveau(
            @PathVariable Long niveauId
    ) {
        return ResponseEntity.ok(service.getByNiveau(niveauId));
    }
}

