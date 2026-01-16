package FLCS.GESTION.CONTROLLER;

import FLCS.GESTION.ENTITEES.Rentree;
import FLCS.GESTION.SERVICE.RentreeService;
import FLCS.GESTION.DTO.RentreeResponse;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;

@Tag(
    name = "Rentrées scolaires",
    description = "Gestion des rentées scolaires (création, consultation)"
)
@RestController
@RequestMapping("/api/rentrees")
public class RentreeController {

    private final RentreeService service;

    public RentreeController(RentreeService service) {
        this.service = service;
    }

    // Création d'une rentrée
    @Operation(
        summary = "Créer une rentrée scolaire",
        description = "Crée une nouvelle année scolaire (ex: SEPTEMBRE 2024)"
    )
    @ApiResponse(responseCode = "200", description = "Rentrée créée avec succès")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Rentree> creer(
            @Valid @RequestBody Rentree rentree
    ) {
        return ResponseEntity.ok(service.creer(rentree));
    }

    // Liste des rentrées
    @Operation(
        summary = "Lister les rentrées scolaires",
        description = "Retourne la liste des années scolaires disponibles"
    )
    @ApiResponse(responseCode = "200", description = "Liste des rentrées")
    @PreAuthorize("hasAnyRole('ADMIN','SECRETAIRE','ENSEIGNANT')")
    @GetMapping
    public ResponseEntity<List<RentreeResponse>> lister() {
        return ResponseEntity.ok(service.lister());
    }

    // Détail d'une rentrée
    @Operation(
        summary = "Consulter une rentrée par ID",
        description = "Retourne les informations détaillées d’une rentrée scolaire"
    )
    @ApiResponse(responseCode = "200", description = "Rentrée trouvée")
    @ApiResponse(responseCode = "404", description = "Rentrée inexistante")
    @PreAuthorize("hasAnyRole('ADMIN','SECRETAIRE','ENSEIGNANT')")
    @GetMapping("/{id}")
    public ResponseEntity<RentreeResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }
}
