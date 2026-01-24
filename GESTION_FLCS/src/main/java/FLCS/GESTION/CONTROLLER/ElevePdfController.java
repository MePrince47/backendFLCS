package FLCS.GESTION.CONTROLLER;

import FLCS.GESTION.EXPORT.ElevePdfExporter;
import FLCS.GESTION.SERVICE.ElevePdfService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/eleves/pdf")
@Tag(name = "Élèves – PDF")
public class ElevePdfController {

    private final ElevePdfService service;
    private final ElevePdfExporter exporter;

    public ElevePdfController(ElevePdfService service, ElevePdfExporter exporter) {
        this.service = service;
        this.exporter = exporter;
    }

    @Operation(summary = "Liste de tous les élèves (PDF)")
    @GetMapping("/tous")
    public ResponseEntity<byte[]> tous() {
        return pdf(
            exporter.exporter(
                service.tous(),
                "LISTE GÉNÉRALE DES ÉLÈVES"
            ),
            "eleves_tous.pdf"
        );
    }

    @Operation(summary = "Liste des élèves par niveau (PDF)")
    @GetMapping("/niveau/{niveauId}")
    public ResponseEntity<byte[]> parNiveau(@PathVariable Long niveauId) {
        return pdf(
            exporter.exporter(
                service.parNiveau(niveauId),
                "LISTE DES ÉLÈVES – NIVEAU"
            ),
            "eleves_niveau.pdf"
        );
    }

    @Operation(summary = "Liste des élèves par rentrée (PDF)")
    @GetMapping("/rentree/{rentreeId}")
    public ResponseEntity<byte[]> parRentree(@PathVariable Long rentreeId) {
        return pdf(
            exporter.exporter(
                service.parRentree(rentreeId),
                "LISTE DES ÉLÈVES – RENTRÉE"
            ),
            "eleves_rentree.pdf"
        );
    }

    // ===== util =====
    private ResponseEntity<byte[]> pdf(byte[] data, String filename) {
        return ResponseEntity.ok()
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + filename
            )
            .contentType(MediaType.APPLICATION_PDF)
            .body(data);
    }
}
