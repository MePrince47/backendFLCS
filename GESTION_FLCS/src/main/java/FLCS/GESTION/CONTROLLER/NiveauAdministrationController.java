package FLCS.GESTION.CONTROLLER;

import FLCS.GESTION.SERVICE.ClotureService;
import FLCS.GESTION.SERVICE.PromotionService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/{id}/promotion")
    public ResponseEntity<String> promouvoir(
        @PathVariable Long id
    ) {
        promotionService.promouvoirNiveau(id);
        return ResponseEntity.ok("Promotion effectuée avec succès");
    }
}
