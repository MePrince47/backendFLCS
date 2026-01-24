package FLCS.GESTION.CONTROLLER;

import FLCS.GESTION.SERVICE.PartenaireService;
import FLCS.GESTION.DTO.PartenaireRequest;
import FLCS.GESTION.DTO.PartenaireResponse;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;

@Tag(
    name = "Partenaires",
    description = "Gestion des partenaires"
)
@RestController
@RequestMapping("/api/partenaires")
public class PartenaireController {

    private final PartenaireService partenaireService;

    public PartenaireController(PartenaireService partenaireService) {
        this.partenaireService = partenaireService;
    }

    @Operation(
        summary = "Créer un partenaire",
        description = "Permet d’enregistrer un nouveau partenaire"
    )
    @ApiResponse(responseCode = "201", description = "Partenaire créé")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PartenaireResponse> create(
            @RequestBody PartenaireRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(partenaireService.create(request));
    }

    @Operation(
        summary = "Lister les partenaires",
        description = "Retourne la liste de tous les partenaires"
    )
    @ApiResponse(responseCode = "200", description = "Liste des partenaires")
    @GetMapping
    public List<PartenaireResponse> getAll() {
        return partenaireService.getAll();
    }

    @Operation(
        summary = "Détail d’un partenaire",
        description = "Retourne les informations d’un partenaire"
    )
    @ApiResponse(responseCode = "200", description = "Partenaire trouvé")
    @ApiResponse(responseCode = "404", description = "Partenaire introuvable")
    @GetMapping("/{id}")
    public PartenaireResponse getById(@PathVariable Long id) {
        return partenaireService.getById(id);
    }
}


