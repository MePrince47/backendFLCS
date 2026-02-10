package FLCS.GESTION.CONTROLLER;

import FLCS.GESTION.SERVICE.EleveService;
import FLCS.GESTION.DTO.SearchResponse;
import FLCS.GESTION.DTO.EleveResponse;
import FLCS.GESTION.DTO.EleveRequest;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/eleves")
@Tag(
    name = "Élèves",
    description = "Gestion des élèves : inscription, consultation, mise à jour et recherche avancée"
)
public class EleveController {

    private final EleveService eleveService;

    public EleveController(EleveService eleveService) {
        this.eleveService = eleveService;
    }

    @Operation(summary = "Inscrire un élève")
    @ApiResponse(responseCode = "201", description = "Élève inscrit avec succès")
    @PostMapping
    public ResponseEntity<EleveResponse> create(@Valid @RequestBody EleveRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(eleveService.create(request));
    }

    @Operation(summary = "Lister tous les élèves")
    @ApiResponse(responseCode = "200", description = "Liste des élèves")
    @GetMapping
    public List<EleveResponse> getAll() {
        return eleveService.getAll();
    }

    @Operation(summary = "Consulter un élève")
    @ApiResponse(responseCode = "200", description = "Élève trouvé")
    @GetMapping("/{id}")
    public EleveResponse getById(@PathVariable Long id) {
        return eleveService.getById(id);
    }

    @Operation(summary = "Modifier un élève")
    @ApiResponse(responseCode = "200", description = "Élève mis à jour")
    @PutMapping("/{id}")
    public ResponseEntity<EleveResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody EleveRequest request
    ) {
        return ResponseEntity.ok(eleveService.update(id, request));
    }

    @Operation(summary = "Supprimer un élève")
    @ApiResponse(responseCode = "204", description = "Élève supprimé")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        eleveService.delete(id);
    }

    @Operation(summary = "Recherche avancée d’élèves")
    @GetMapping("/search")
    public ResponseEntity<SearchResponse<EleveResponse>> rechercheAvancee(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String niveauScolaire,
            @RequestParam(required = false) String rentree,
            @RequestParam(required = false) String niveauLangue,
            @RequestParam(required = false) String partenaire
    ) {
        return ResponseEntity.ok(
                eleveService.rechercheAvancee(
                        nom, niveauScolaire, rentree, niveauLangue, partenaire
                )
        );
    }

    @Operation(summary = "Lister les élèves par niveau")
    @GetMapping("/niveau/{niveauId}")
    public List<EleveResponse> getByNiveau(@PathVariable Long niveauId) {
        return eleveService.getByNiveau(niveauId);
    }

    @Operation(summary = "Lister les élèves d’une rentrée")
    @GetMapping("/rentree/{rentreeId}")
    public List<EleveResponse> getByRentree(@PathVariable Long rentreeId) {
        return eleveService.getByRentree(rentreeId);
    }
}