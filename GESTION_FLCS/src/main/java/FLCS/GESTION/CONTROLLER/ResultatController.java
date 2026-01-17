package FLCS.GESTION.CONTROLLER;

import FLCS.GESTION.ENTITEES.Resultat;
import FLCS.GESTION.SERVICE.ResultatService;
import FLCS.GESTION.EXPORT.ResultatPdfExporter;
import FLCS.GESTION.DTO.ResultatResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PostMapping("/niveau/{niveauId}")
    public List<ResultatResponse> genererPourNiveau(
        @PathVariable Long niveauId
    ) {
        return resultatService.genererPourNiveau(niveauId);
    }

    /**
     * Télécharger le PDF des résultats d’un niveau
     */
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

