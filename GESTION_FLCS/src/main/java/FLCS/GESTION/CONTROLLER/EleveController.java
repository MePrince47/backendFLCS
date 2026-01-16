package FLCS.GESTION.CONTROLLER;

import FLCS.GESTION.ENTITEES.Eleve;
import FLCS.GESTION.SERVICE.EleveService;
import FLCS.GESTION.DTO.SearchResponse;
import FLCS.GESTION.DTO.EleveResponse;
import FLCS.GESTION.DTO.EleveRequest;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;

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

    @Operation(
        summary = "Inscrire un élève",
        description = "Permet d’inscrire un nouvel élève dans le système scolaire"
    )
    @ApiResponse(responseCode = "201", description = "Élève inscrit avec succès")
    @PreAuthorize("hasAnyRole('ADMIN','SECRETAIRE')")
    @PostMapping
    public ResponseEntity<EleveResponse> create(
            @Valid @RequestBody EleveRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(eleveService.create(request));
    }

    @Operation(
        summary = "Lister tous les élèves",
        description = "Retourne la liste complète des élèves enregistrés"
    )
    @ApiResponse(responseCode = "200", description = "Liste des élèves")
    @PreAuthorize("hasAnyRole('ADMIN','SECRETAIRE','ENSEIGNANT')")
    @GetMapping
    public List<EleveResponse> getAll() {
        return eleveService.getAll();
    }

    @Operation(
        summary = "Consulter un élève",
        description = "Retourne les informations détaillées d’un élève par son identifiant"
    )
    @ApiResponse(responseCode = "200", description = "Élève trouvé")
    @ApiResponse(responseCode = "404", description = "Élève introuvable")
    @PreAuthorize("hasAnyRole('ADMIN','SECRETAIRE','ENSEIGNANT')")
    @GetMapping("/{id}")
    public EleveResponse getById(@PathVariable Long id) {
        return eleveService.getById(id);
    }

    @Operation(
        summary = "Modifier un élève",
        description = "Met à jour les informations administratives d’un élève"
    )
    @ApiResponse(responseCode = "200", description = "Élève mis à jour")
    @PreAuthorize("hasAnyRole('ADMIN','SECRETAIRE')")
    @PutMapping("/{id}")
    public ResponseEntity<EleveResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody EleveRequest request
    ) {
        return ResponseEntity.ok(eleveService.update(id, request));
    }


    @Operation(
        summary = "Supprimer un élève",
        description = "Supprime définitivement un élève du système"
    )
    @ApiResponse(responseCode = "204", description = "Élève supprimé")
    @PreAuthorize("hasAnyRole('ADMIN','SECRETAIRE')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        eleveService.delete(id);
    }

    @Operation(
        summary = "Recherche avancée d’élèves",
        description = "Recherche multicritère : nom, niveau scolaire, rentrée, langue, partenaire"
    )
    @ApiResponse(responseCode = "200", description = "Résultat de la recherche")
    @PreAuthorize("hasAnyRole('ADMIN','SECRETAIRE','ENSEIGNANT')")
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

    @Operation(
        summary = "Recherche avancée d’élèves",
        description = "Recherche multicritère : nom, niveau scolaire, rentrée, langue, partenaire"
    )
    @ApiResponse(responseCode = "200", description = "Résultat de la recherche")
    @PreAuthorize("hasAnyRole('ADMIN','SECRETAIRE','ENSEIGNANT')")
    @GetMapping("/niveau/{niveauId}")
    public List<EleveResponse> getByNiveau(@PathVariable Long niveauId) {
        return eleveService.getByNiveau(niveauId);
    }

    @Operation(
        summary = "Lister les élèves d’une rentrée",
        description = "Retourne tous les élèves associés à une rentrée scolaire"
    )
    @ApiResponse(responseCode = "200", description = "Liste des élèves de la rentrée")
    @PreAuthorize("hasAnyRole('ADMIN','SECRETAIRE','ENSEIGNANT')")
    @GetMapping("/rentree/{rentreeId}")
    public List<EleveResponse> getByRentree(@PathVariable Long rentreeId) {
        return eleveService.getByRentree(rentreeId);
    }



}
