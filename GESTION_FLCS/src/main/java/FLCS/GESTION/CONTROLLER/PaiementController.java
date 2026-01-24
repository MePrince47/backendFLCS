package FLCS.GESTION.CONTROLLER;

import FLCS.GESTION.SERVICE.PaiementService;
import FLCS.GESTION.DTO.PaiementResponse;
import FLCS.GESTION.DTO.PaiementRequest;
import FLCS.GESTION.DTO.PaiementResumeResponse;
import FLCS.GESTION.DTO.PaiementHistoriqueResponse;
import FLCS.GESTION.DTO.PaiementExport;
import FLCS.GESTION.EXPORT.PaiementPdfGenerator;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;


@Tag(
    name = "Paiements",
    description = "Gestion des paiements, situations financières et export PDF"
)
@RestController
@RequestMapping("/api/paiements")
public class PaiementController {

    private final PaiementService paiementService;
    private final PaiementPdfGenerator paiementPdfGenerator;

     public PaiementController(
        PaiementService paiementService,
        PaiementPdfGenerator paiementPdfGenerator
    ) {
        this.paiementService = paiementService;
        this.paiementPdfGenerator = paiementPdfGenerator;
    }

    @Operation(
        summary = "Enregistrer un paiement",
        description = "Permet d’enregistrer un paiement pour un élève"
    )
    @ApiResponse(responseCode = "201", description = "Paiement enregistré avec succès")
    @PreAuthorize("hasAnyRole('ADMIN','SECRETAIRE')")
    @PostMapping
    public ResponseEntity<PaiementResponse> create(
            @RequestBody PaiementRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paiementService.create(request));
    }

    @Operation(
        summary = "Liste des paiements d’un élève",
        description = "Retourne tous les paiements effectués par un élève"
    )
    @ApiResponse(responseCode = "200", description = "Liste des paiements")
    @PreAuthorize("hasAnyRole('ADMIN','SECRETAIRE')")
    @GetMapping("/eleve/{eleveId}")
    public List<PaiementResponse> getByEleve(@PathVariable Long eleveId) {
        return paiementService.getByEleve(eleveId);
    }

    // GET_INFOS SUR LES PAIEMENTS D'UN ELEVE
    @Operation(
        summary = "Résumé des paiements",
        description = "Retourne la situation financière globale d’un élève (payé, restant, statut)"
    )
    @ApiResponse(responseCode = "200", description = "Résumé financier")
    @PreAuthorize("hasAnyRole('ADMIN','SECRETAIRE')")
    @GetMapping("/eleves/{id}/resume")
    public PaiementResumeResponse resumePaiement(@PathVariable Long id) {
        return paiementService.getResumePaiement(id);
    }

    // GET_INFOS SUR LES PAIEMENTS DE TOUT LES ELEVES
    @Operation(
        summary = "Résumé des paiements",
        description = "Retourne la situation financière globale de tout les élèves (payé, restant, statut)"
    )
    @ApiResponse(responseCode = "200", description = "Résumé financier")
    @PreAuthorize("hasAnyRole('ADMIN','SECRETAIRE')")
    @GetMapping("/eleves/resume")
    public List<PaiementResumeResponse> getResumePaiementsTousEleves() {
        return paiementService.getResumePaiementsTousEleves();
    }

    // GET HISTORIQUE DE PAIEMENTS D'UN ELEVE
    @Operation(
        summary = "Historique des paiements",
        description = "Retourne l’historique détaillé des paiements d’un élève"
    )
    @ApiResponse(responseCode = "200", description = "Historique des paiements")
    @PreAuthorize("hasAnyRole('ADMIN','SECRETAIRE')")
    @GetMapping("/eleves/{id}/paiements")
    public List<PaiementHistoriqueResponse> historique(@PathVariable Long id) {
        return paiementService.historique(id);
    }

    // EXPORT PDF
    @Operation(
        summary = "Exporter les paiements en PDF",
        description = "Génère un document PDF officiel des paiements d’un élève"
    )
    @ApiResponse(responseCode = "200", description = "PDF généré")
    @PreAuthorize("hasAnyRole('ADMIN','SECRETAIRE')")
    @GetMapping(
        value = "/paiements/export/pdf/{eleveId}",
        produces = MediaType.APPLICATION_PDF_VALUE
    )
    public ResponseEntity<byte[]> exportPdf(@PathVariable Long eleveId) {

        PaiementExport dto = paiementService.exportPaiements(eleveId);
        byte[] pdf = paiementPdfGenerator.generate(dto);

        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=paiement.pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdf);
    }



}
