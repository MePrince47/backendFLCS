package FLCS.GESTION.CONTROLLER;

import FLCS.GESTION.ENTITEES.Rentree;
import FLCS.GESTION.SERVICE.RentreeService;
import FLCS.GESTION.DTO.RentreeResponse;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentrees")
public class RentreeController {

    private final RentreeService service;

    public RentreeController(RentreeService service) {
        this.service = service;
    }

    // Création d'une rentrée
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Rentree> creer(
            @Valid @RequestBody Rentree rentree
    ) {
        return ResponseEntity.ok(service.creer(rentree));
    }

    // Liste des rentrées
    @PreAuthorize("hasAnyRole('ADMIN','SECRETAIRE','ENSEIGNANT')")
    @GetMapping
    public ResponseEntity<List<RentreeResponse>> lister() {
        return ResponseEntity.ok(service.lister());
    }

    // Détail d'une rentrée
    @PreAuthorize("hasAnyRole('ADMIN','SECRETAIRE','ENSEIGNANT')")
    @GetMapping("/{id}")
    public ResponseEntity<RentreeResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }
}
