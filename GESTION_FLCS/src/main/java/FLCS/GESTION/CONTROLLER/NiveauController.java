package FLCS.GESTION.CONTROLLER;

import FLCS.GESTION.ENTITEES.Niveau;
import FLCS.GESTION.SERVICE.NiveauService;
import FLCS.GESTION.DTO.NiveauResponse;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/niveaux")
@Tag(
    name = "Niveaux",
    description = "Gestion des niveaux académiques (création, consultation, organisation)"
)
public class NiveauController {

    private final NiveauService service;

    public NiveauController(NiveauService service) {
        this.service = service;
    }

    // Création d'un niveau (indépendant)
    @Operation(
        summary = "Créer un niveau académique indépendant",
        description = "Permet de créer un nouveau niveau (B2_mischung, C1)"
    )
    @ApiResponse(responseCode = "200", description = "Niveau créé avec succès")
    @PostMapping
    public ResponseEntity<NiveauResponse> creer(
            @Valid @RequestBody Niveau niveau
    ) {
        return ResponseEntity.ok(service.creer(niveau));
    }

    // Tous les niveaux
    @Operation(
        summary = "Lister tous les niveaux",
        description = "Retourne l’ensemble des niveaux enregistrés dans le système"
    )
    @ApiResponse(responseCode = "200", description = "Liste des niveaux")
    @GetMapping
    public ResponseEntity<List<NiveauResponse>> lister() {
        return ResponseEntity.ok(service.lister());
    }

    // Niveaux indépendants
    @Operation(
        summary = "Lister les niveaux indépendants",
        description = "Retourne les niveaux qui ne sont pas liés à une promotion"
    )
    @ApiResponse(responseCode = "200", description = "Liste des niveaux indépendants")
    @GetMapping("/independants")
    public ResponseEntity<List<NiveauResponse>> independants() {
        return ResponseEntity.ok(service.listerNiveauxIndependants());
    }

    // Détail niveau
    @Operation(
        summary = "Consulter un niveau par ID",
        description = "Retourne les informations détaillées d’un niveau"
    )
    @ApiResponse(responseCode = "200", description = "Niveau trouvé")
    @ApiResponse(responseCode = "404", description = "Niveau inexistant")
    @GetMapping("/{id}")
    public ResponseEntity<NiveauResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }
}