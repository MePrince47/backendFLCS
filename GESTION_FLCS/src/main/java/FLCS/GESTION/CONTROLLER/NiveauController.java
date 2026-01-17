package FLCS.GESTION.CONTROLLER;

import FLCS.GESTION.ENTITEES.Niveau;
import FLCS.GESTION.SERVICE.NiveauService;
import FLCS.GESTION.DTO.NiveauResponse;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/niveaux")
public class NiveauController {

    private final NiveauService service;

    public NiveauController(NiveauService service) {
        this.service = service;
    }

    // Création d'un niveau (indépendant)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<NiveauResponse> creer(
            @Valid @RequestBody Niveau niveau
    ) {
        return ResponseEntity.ok(service.creer(niveau));
    }

    // Tous les niveaux
    @PreAuthorize("hasAnyRole('ADMIN','SECRETAIRE','ENSEIGNANT')")
    @GetMapping
    public ResponseEntity<List<NiveauResponse>> lister() {
        return ResponseEntity.ok(service.lister());
    }

    // Niveaux indépendants
    @PreAuthorize("hasAnyRole('ADMIN','SECRETAIRE','ENSEIGNANT')")
    @GetMapping("/independants")
    public ResponseEntity<List<NiveauResponse>> independants() {
        return ResponseEntity.ok(service.listerNiveauxIndependants());
    }

    // Détail niveau
    @PreAuthorize("hasAnyRole('ADMIN','SECRETAIRE','ENSEIGNANT')")
    @GetMapping("/{id}")
    public ResponseEntity<NiveauResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }
}
