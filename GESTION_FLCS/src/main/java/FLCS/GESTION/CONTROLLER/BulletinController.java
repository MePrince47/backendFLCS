package FLCS.GESTION.CONTROLLER;

import FLCS.GESTION.DTO.BulletinDTO;
import FLCS.GESTION.EXPORT.BulletinPdfExporter;
import FLCS.GESTION.SERVICE.BulletinService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bulletins")
@Tag(name = "Bulletins", description = "Génération et export des bulletins officiels")
public class BulletinController {

    private final BulletinService bulletinService;
    private final BulletinPdfExporter pdfExporter;

    public BulletinController(
            BulletinService bulletinService,
            BulletinPdfExporter pdfExporter
    ) {
        this.bulletinService = bulletinService;
        this.pdfExporter = pdfExporter;
    }

    // ===================== JSON =====================

    @Operation(
        summary = "Générer le bulletin d'un élève (JSON)",
        description = "Retourne toutes les notes, moyennes et décision finale"
    )
    @ApiResponse(responseCode = "200", description = "Bulletin généré avec succès")
    @GetMapping("/{eleveId}/niveau/{niveauId}")
    public BulletinDTO genererBulletin(
            @PathVariable Long eleveId,
            @PathVariable Long niveauId
    ) {
        return bulletinService.generer(eleveId, niveauId);
    }

    // ===================== PDF =====================

    @Operation(
        summary = "Télécharger le bulletin en PDF",
        description = "Génère le bulletin officiel au format PDF"
    )
    @ApiResponse(responseCode = "200", description = "PDF généré avec succès")
    @GetMapping(
        value = "/{eleveId}/niveau/{niveauId}/pdf",
        produces = MediaType.APPLICATION_PDF_VALUE
    )
    public ResponseEntity<byte[]> exporterPdf(
            @PathVariable Long eleveId,
            @PathVariable Long niveauId
    ) {

        BulletinDTO bulletin = bulletinService.generer(eleveId, niveauId);
        byte[] pdf = pdfExporter.generer(bulletin);

        return ResponseEntity.ok()
                .header(
                    HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=bulletin_" + eleveId + ".pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
