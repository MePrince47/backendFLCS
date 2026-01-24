package FLCS.GESTION.CONTROLLER;

import FLCS.GESTION.SERVICE.ResultatService;
import FLCS.GESTION.EXPORT.ResultatPdfExporter;
import FLCS.GESTION.DTO.ResultatResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;

@Tag(
    name = "Résultats",
    description = "Génération des résultats finaux et export PDF"
)
@RestController
@RequestMapping("/api/resultats")
public class ResultatController {

    private final ResultatService resultatService;
    private final ResultatPdfExporter pdfExporter;

    public ResultatController(
        ResultatService resultatService,
        ResultatPdfExporter pdfExporter
    ) {
        this.resultatService = resultatService;
        this.pdfExporter = pdfExporter;
    }

    /**
     * Générer le résultat FINAL d’un élève
     */
    @Operation(
        summary = "Générer le résultat final d’un élève",
        description = "Calcule le résultat final d’un élève à partir des notes hebdomadaires et finales"
    )
    @ApiResponse(responseCode = "200", description = "Résultat généré")
    @PostMapping("/eleve/{eleveId}/niveau/{niveauId}")
    public ResultatResponse genererPourEleve(
        @PathVariable Long eleveId,
        @PathVariable Long niveauId
    ) {
        return resultatService.genererPourEleve(eleveId, niveauId);
    }

    /**
     * Générer les résultats de toute une classe
     */
    @Operation(
        summary = "Générer les résultats d’un niveau",
        description = "Calcule les résultats finaux pour tous les élèves d’un niveau"
    )
    @ApiResponse(responseCode = "200", description = "Résultats générés")
    @PostMapping("/niveau/{niveauId}")
    public List<ResultatResponse> genererPourNiveau(
        @PathVariable Long niveauId
    ) {
        return resultatService.genererPourNiveau(niveauId);
    }

    /**
     * Télécharger le PDF des résultats d’un niveau
     */
    @Operation(
    summary = "Télécharger les résultats en PDF",
    description = "Génère et télécharge le PDF officiel des résultats d’un niveau"
    )
    @ApiResponse(responseCode = "200", description = "PDF généré")
    @GetMapping(
        value = "/niveau/{niveauId}/pdf",
        produces = MediaType.APPLICATION_PDF_VALUE
    )
    public ResponseEntity<byte[]> pdfPourNiveau(
        @PathVariable Long niveauId
    ) {
        byte[] pdf = pdfExporter.genererPdfPourNiveau(niveauId);

        return ResponseEntity.ok()
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=resultats-niveau-" + niveauId + ".pdf"
            )
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdf);
    }
}

