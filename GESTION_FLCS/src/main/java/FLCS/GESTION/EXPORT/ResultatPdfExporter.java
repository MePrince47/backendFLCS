package FLCS.GESTION.EXPORT;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import FLCS.GESTION.ENTITEES.Resultat;
import FLCS.GESTION.REPOSITORY.ResultatRepository;

import org.springframework.stereotype.Component;


import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

@Component
public class ResultatPdfExporter {

    private final ResultatRepository resultatRepo;

    public ResultatPdfExporter(ResultatRepository resultatRepo) {
        this.resultatRepo = resultatRepo;
    }

    public byte[] genererPdfPourNiveau(Long niveauId) {

        List<Resultat> resultats =
            resultatRepo.findByNiveau_Id(niveauId);

        if (resultats.isEmpty()) {
            throw new IllegalStateException("Aucun résultat généré");
        }

        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // ===== LOGO =====
            Image logo = Image.getInstance(
                getClass().getResource("/static/FLC-logo-04.png")
            );
            logo.scaleToFit(120, 120);
            logo.setAlignment(Image.ALIGN_CENTER);
            document.add(logo);
            
            // ================= TITRE =================
            Paragraph titre = new Paragraph(
                "FLCS – RÉSULTATS OFFICIELS\nNiveau " +
                resultats.get(0).getNiveau().getCode(),
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)
            );
            titre.setAlignment(Element.ALIGN_CENTER);
            document.add(titre);

            document.add(Chunk.NEWLINE);

            // ================= TABLE 1 : ENDPRÜFUNG =================
            document.add(sectionTitle("NOTES ENDPRÜFUNG"));

            PdfPTable tableEnd = creerTableEndprufung();
            for (Resultat r : resultats) {
                ajouterLigneEndprufung(tableEnd, r);
            }
            document.add(tableEnd);

            document.add(Chunk.NEWLINE);

            // ================= TABLE 2 : RÉSULTATS FINAUX =================
            document.add(sectionTitle("RÉSULTATS FINAUX"));

            PdfPTable tableFinal = creerTableFinale();
            for (Resultat r : resultats) {
                ajouterLigneFinale(tableFinal, r);
            }
            document.add(tableFinal);

            document.add(Chunk.NEWLINE);

            // ================= STATS CLASSE =================
            ajouterStatistiquesClasse(document, resultats);

            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);

            // ===== SIGNATURES =====
            PdfPTable signature = new PdfPTable(2);
            signature.setWidthPercentage(100);

            PdfPCell left = new PdfPCell(new Phrase(
                "Responsable pédagogique\n\nSignature : ____________"
            ));
            left.setBorder(Rectangle.NO_BORDER);

            PdfPCell right = new PdfPCell(new Phrase(
                "Direction FLCS\n\nCachet & signature"
            ));
            right.setBorder(Rectangle.NO_BORDER);
            right.setHorizontalAlignment(Element.ALIGN_RIGHT);

            signature.addCell(left);
            signature.addCell(right);

            document.add(signature);

            document.close();
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erreur génération PDF", e);
        }

    }

    // =========================================================
    // ======================= TABLES ==========================
    // =========================================================

    private PdfPTable creerTableEndprufung() {
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);

        headers(table,
            "Élève", "Lesen", "Hören",
            "Schreiben", "Grammatik", "Sprechen"
        );
        return table;
    }

    private PdfPTable creerTableFinale() {
        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);

        headers(table,
            "Élève",
            "Moy. Lesen", "Moy. Hören",
            "Moy. Schreiben", "Moy. Grammatik",
            "Moy. Sprechen",
            "Moyenne Générale",
            "Décision"
        );
        return table;
    }


    // =========================================================
    // ======================= LIGNES ==========================
    // =========================================================

    private void ajouterLigneEndprufung(PdfPTable table, Resultat r) {

        table.addCell(cell(r.getEleve().getNom() + " " + r.getEleve().getPrenom()));

        table.addCell(noteCell(r.getEndLes(), r));
        table.addCell(noteCell(r.getEndHor(), r));
        table.addCell(noteCell(r.getEndSchreib(), r));
        table.addCell(noteCell(r.getEndGramm(), r));
        table.addCell(noteCell(r.getEndSpre(), r));
    }

    private void ajouterLigneFinale(PdfPTable table, Resultat r) {

        table.addCell(cell(r.getEleve().getNom() + " " + r.getEleve().getPrenom()));

        table.addCell(noteCell(r.getMoyLes(), r));
        table.addCell(noteCell(r.getMoyHor(), r));
        table.addCell(noteCell(r.getMoySchreib(), r));
        table.addCell(noteCell(r.getMoyGramm(), r));
        table.addCell(noteCell(r.getMoySpre(), r));

        table.addCell(noteCell(r.getMoyenneGenerale(), r));

        PdfPCell decision = new PdfPCell(
            new Phrase(r.isAdmis() ? "ADMIS" : "AJOURNÉ")
        );
        decision.setBackgroundColor(
            r.isAdmis() ? Color.GREEN : Color.PINK
        );
        decision.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(decision);
    }

    // =========================================================
    // ======================= UTILS ===========================
    // =========================================================

    private void headers(PdfPTable table, String... titles) {
        Stream.of(titles).forEach(h -> {
            PdfPCell cell = new PdfPCell(
                new Phrase(h, FontFactory.getFont(FontFactory.HELVETICA_BOLD))
            );
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        });
    }

    private PdfPCell noteCell(Double note, Resultat r) {
        PdfPCell cell = new PdfPCell(
            new Phrase(val(note))
        );
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        if (note != null) {
            double seuil =
                r.getNiveau().getCode().startsWith("A") ? 12 : 60;

            cell.setBackgroundColor(
                note >= seuil ? new Color(200, 255, 200)
                              : new Color(255, 200, 200)
            );
        }
        return cell;
    }

    private PdfPCell cell(String txt) {
        PdfPCell cell = new PdfPCell(new Phrase(txt));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        return cell;
    }

    private String val(Double d) {
        return d == null ? "-" : String.format("%.2f", d);
    }

    private Paragraph sectionTitle(String txt) {
        Paragraph p = new Paragraph(
            txt,
            FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)
        );
        p.setSpacingAfter(5);
        return p;
    }

    // =========================================================
    // =================== STATS CLASSE ========================
    // =========================================================

    private void ajouterStatistiquesClasse(
        Document document,
        List<Resultat> resultats
    ) throws DocumentException {

        double moyenneClasse =
            resultats.stream()
                .mapToDouble(Resultat::getMoyenneGenerale)
                .average()
                .orElse(0);

        Resultat premier =
            resultats.stream()
                .max((a, b) ->
                    Double.compare(
                        a.getMoyenneGenerale(),
                        b.getMoyenneGenerale()))
                .orElseThrow();

        long admis =
            resultats.stream().filter(Resultat::isAdmis).count();

        document.add(new Paragraph(
            "Moyenne de la classe : " +
            String.format("%.2f", moyenneClasse)
        ));

        document.add(new Paragraph(
            "Premier de la classe : " +
            premier.getEleve().getNom() + " " +
            premier.getEleve().getPrenom()
        ));

        document.add(new Paragraph(
            "Taux de réussite : " +
            (admis * 100 / resultats.size()) + " %"
        ));
    }
}
