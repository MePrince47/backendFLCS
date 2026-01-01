package FLCS.GESTION.Controllers;

import FLCS.GESTION.Entitees.Eleve;
import FLCS.GESTION.Entitees.Niveau;
import FLCS.GESTION.Dtos.response.EvaluationHebdomadaireResponse;
import FLCS.GESTION.Dtos.response.EndpruefungResponse;
import FLCS.GESTION.Repository.EleveRepository;
import FLCS.GESTION.Repository.NiveauRepository;
import FLCS.GESTION.Services.EvaluationHebdomadaireService;
import FLCS.GESTION.Services.EndpruefungService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bulletins")
public class BulletinController {

    private final EleveRepository eleveRepository;
    private final NiveauRepository niveauRepository;
    private final EvaluationHebdomadaireService evaluationHebdoService;
    private final EndpruefungService endpruefungService;

    public BulletinController(EleveRepository eleveRepository,
            NiveauRepository niveauRepository,
            EvaluationHebdomadaireService evaluationHebdoService,
            EndpruefungService endpruefungService) {
        this.eleveRepository = eleveRepository;
        this.niveauRepository = niveauRepository;
        this.evaluationHebdoService = evaluationHebdoService;
        this.endpruefungService = endpruefungService;
    }

    @GetMapping("/{eleveId}/{niveauId}/pdf")
    public ResponseEntity<InputStreamResource> genererBulletinPdf(
            @PathVariable Long eleveId,
            @PathVariable Long niveauId) throws IOException {

        // Récupération des données
        Eleve eleve = eleveRepository.findById(eleveId).orElse(null);
        Niveau niveau = niveauRepository.findById(niveauId).orElse(null);
        List<EvaluationHebdomadaireResponse> notesAll = evaluationHebdoService.getEvaluationsByEleve(eleveId);
        List<EvaluationHebdomadaireResponse> notesHebdo = notesAll.stream()
                .filter(n -> n.getNiveauId() != null && n.getNiveauId().equals(niveauId))
                .toList();
        EndpruefungResponse endpruefung = endpruefungService.getEndpruefungByEleveAndNiveau(eleveId, niveauId);

        // Calcul des moyennes selon le niveau
        Double moyenneFinale = calculerMoyenneFinale(niveau != null ? niveau.getNom() : null, notesHebdo,
                endpruefung);

        // Génération PDF
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // En-tête
        document.add(new Paragraph("FLCS - Bulletin de Notes").setBold().setFontSize(16));
        document.add(new Paragraph("Élève: " + (eleve != null ? eleve.getNom() + " " + eleve.getPrenom() : "-")));
        document.add(new Paragraph("Niveau: " + (niveau != null ? niveau.getNom() : "-")));

        // Tableau des notes hebdomadaires
        Table table = new Table(6);
        table.addHeaderCell("Semaine");
        table.addHeaderCell("Lesen");
        table.addHeaderCell("Hören");
        table.addHeaderCell("Schreiben");
        table.addHeaderCell("Sprechen");
        table.addHeaderCell("Grammatik");

        for (EvaluationHebdomadaireResponse eh : notesHebdo) {
            table.addCell(eh.getSemaine() != null ? eh.getSemaine().toString() : "-");
            table.addCell(eh.getNoteLesen() != null ? eh.getNoteLesen().toString() : "-");
            table.addCell(eh.getNoteHoren() != null ? eh.getNoteHoren().toString() : "-");
            table.addCell(eh.getNoteSchreiben() != null ? eh.getNoteSchreiben().toString() : "-");
            table.addCell(eh.getNoteSprechen() != null ? eh.getNoteSprechen().toString() : "-");
            table.addCell(eh.getNoteGrammatik() != null ? eh.getNoteGrammatik().toString() : "-");
        }

        document.add(table);

        // Note finale
        document.add(new Paragraph("Moyenne finale: " + moyenneFinale).setBold());

        document.close();

        // Téléchargement
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(baos.toByteArray()));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=bulletin_" + eleveId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    private Double calculerMoyenneFinale(String nomNiveau, List<EvaluationHebdomadaireResponse> notesHebdo,
            EndpruefungResponse endpruefung) {
        if (nomNiveau == null)
            return 0.0;
        if (nomNiveau.equals("A1") || nomNiveau.equals("A2")) {
            double moyenneHebdo = notesHebdo.stream()
                    .mapToDouble(eh -> {
                        double somme = (eh.getNoteLesen() != null ? eh.getNoteLesen() : 0.0)
                                + (eh.getNoteHoren() != null ? eh.getNoteHoren() : 0.0)
                                + (eh.getNoteSchreiben() != null ? eh.getNoteSchreiben() : 0.0)
                                + (eh.getNoteSprechen() != null ? eh.getNoteSprechen() : 0.0)
                                + (eh.getNoteGrammatik() != null ? eh.getNoteGrammatik() : 0.0);
                        return somme / 5.0;
                    })
                    .average()
                    .orElse(0.0);

            double noteExamen = endpruefung != null && endpruefung.getMoyenne() != null ? endpruefung.getMoyenne()
                    : 0.0;
            return (moyenneHebdo * 0.4) + (noteExamen * 0.6);
        } else if (nomNiveau.equals("B1") || nomNiveau.equals("B2")) {
            return endpruefung != null && endpruefung.getMoyenne() != null ? endpruefung.getMoyenne() : 0.0;
        } else {
            return 0.0;
        }
    }
}