package FLCS.GESTION.CONTROLLER;

import FLCS.GESTION.SERVICE.ClotureService;
import FLCS.GESTION.SERVICE.PromotionService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Tag(
    name = "Niveaux – Administration",
    description = "Actions administratives : clôture des niveaux et promotion des élèves"
)
@RestController
@RequestMapping("/api/niveaux")
public class NiveauAdministrationController {

    private final ClotureService clotureService;
    private final PromotionService promotionService;

    public NiveauAdministrationController(
        ClotureService clotureService,
        PromotionService promotionService
    ) {
        this.clotureService = clotureService;
        this.promotionService = promotionService;
    }

    /**
     *  Clôturer un niveau
     */
    @Operation(
        summary = "Clôturer un niveau",
        description = "Clôture un niveau académique. Aucune modification n’est possible après cette action."
    )
    @ApiResponse(responseCode = "200", description = "Niveau clôturé avec succès")
    @PostMapping("/{id}/cloture")
    public ResponseEntity<String> cloturer(
        @PathVariable Long id
    ) {
        clotureService.cloturerNiveau(id);
        return ResponseEntity.ok("Niveau clôturé avec succès");
    }

    /**
     * Promouvoir les élèves admis
     */
    @Operation(
        summary = "Promouvoir les élèves d’un niveau",
        description = "Promouvoit automatiquement les élèves admis vers le niveau supérieur"
    )
    @ApiResponse(responseCode = "200", description = "Promotion effectuée avec succès")
    @PostMapping("/{id}/promotion")
    public ResponseEntity<String> promouvoir(
        @PathVariable Long id
    ) {
        promotionService.promouvoirNiveau(id);
        return ResponseEntity.ok("Promotion effectuée avec succès");
    }
}
