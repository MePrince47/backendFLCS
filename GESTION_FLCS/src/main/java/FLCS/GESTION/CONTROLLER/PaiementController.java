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
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;


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

    @PreAuthorize("hasAnyRole('ADMIN','SECRETAIRE')")
    @PostMapping
    public ResponseEntity<PaiementResponse> create(
            @RequestBody PaiementRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paiementService.create(request));
    }

    @GetMapping("/eleve/{eleveId}")
    public List<PaiementResponse> getByEleve(@PathVariable Long eleveId) {
        return paiementService.getByEleve(eleveId);
    }

    // GET_INFOS SUR LES PAIEMENTS D'UN ELEVE
    @PreAuthorize("hasAnyRole('ADMIN','SECRETAIRE')")
    @GetMapping("/eleves/{id}/resume")
    public PaiementResumeResponse resumePaiement(@PathVariable Long id) {
        return paiementService.getResumePaiement(id);
    }

    // GET HISTORIQUE DE PAIEMENTS D'UN ELEVE
    @GetMapping("/eleves/{id}/paiements")
    @PreAuthorize("hasAnyRole('ADMIN','SECRETAIRE')")
    public List<PaiementHistoriqueResponse> historique(@PathVariable Long id) {
        return paiementService.historique(id);
    }

    // EXPORT PDF
    @GetMapping("/paiements/export/pdf/{eleveId}")
    @PreAuthorize("hasAnyRole('ADMIN','SECRETAIRE')")
    public ResponseEntity<byte[]> exportPdf(@PathVariable Long eleveId) {

        PaiementExport dto = paiementService.exportPaiements(eleveId);
        byte[] pdf = paiementPdfGenerator.generate(dto);

        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=paiement.pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdf);
    }



}
