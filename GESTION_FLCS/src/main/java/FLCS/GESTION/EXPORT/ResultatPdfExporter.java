package FLCS.GESTION.EXPORT;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import FLCS.GESTION.ENTITEES.Resultat;
import FLCS.GESTION.REPOSITORY.ResultatRepository;

import org.springframework.stereotype.Component;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Component
public class ResultatPdfExporter {

    private final ResultatRepository resultatRepo;

    public ResultatPdfExporter(ResultatRepository resultatRepo) {
        this.resultatRepo = resultatRepo;
    }

    // =========================================================
    // ===================== GENERATION ========================
    // =========================================================

    public byte[] genererPdfPourNiveau(Long niveauId) {

        List<Resultat> resultats = resultatRepo.findByNiveau_Id(niveauId);

        if (resultats.isEmpty()) {
            throw new IllegalStateException("Aucun résultat généré");
        }

        String codeComplet = resultats.get(0).getNiveau().getCode();
        String codeNiveau = codeComplet.split("_")[0];

        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // ================= LOGO =================
            Image logo = Image.getInstance(
                getClass().getResource("/static/FLC-logo-04.png")
            );
            logo.scaleToFit(120, 120);
            logo.setAlignment(Image.ALIGN_CENTER);
            document.add(logo);

            // ================= TITRE =================
            Paragraph titre = new Paragraph(
                "FLCS – RÉSULTATS OFFICIELS\nNiveau " + codeComplet,
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)
            );
            titre.setAlignment(Element.ALIGN_CENTER);
            document.add(titre);
            document.add(Chunk.NEWLINE);

            // ================= TABLES =================
            if (codeNiveau.equals("A1")) {

                document.add(sectionTitle("RÉSULTATS DÉTAILLÉS"));
                PdfPTable t = creerTableA1();
                resultats.forEach(r -> ajouterLigneA1(t, r));
                document.add(t);

            } else if (codeNiveau.equals("A2")) {

                document.add(sectionTitle("RÉSULTATS DÉTAILLÉS"));
                PdfPTable t = creerTableA2();
                resultats.forEach(r -> ajouterLigneA2(t, r));
                document.add(t);

            } else if (codeNiveau.equals("B1") || codeNiveau.equals("B2")) {

                boolean avecSoutenance = codeNiveau.equals("B2");

                document.add(sectionTitle("NOTES ENDPRÜFUNG"));
                PdfPTable end = creerTableEndprufung();
                resultats.forEach(r -> ajouterLigneEndprufung(end, r));
                document.add(end);

                document.add(Chunk.NEWLINE);

                document.add(sectionTitle("RÉSULTATS GÉNÉRAUX"));
                PdfPTable fin = creerTableResultatsB1B2(avecSoutenance);
                resultats.forEach(r -> ajouterLigneResultatsB1B2(fin, r, avecSoutenance));
                document.add(fin);
            }

            document.add(Chunk.NEWLINE);
            ajouterStatistiquesClasse(document, resultats);
            ajouterSignatures(document);

            document.close();
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erreur génération PDF", e);
        }
    }

    // =========================================================
    // ======================= TABLES ==========================
    // =========================================================

    private PdfPTable creerTableA1() {
        PdfPTable t = new PdfPTable(9);
        t.setWidthPercentage(100);
        headers(t,
            "Élève",
            "Lesen","Hören","Schreiben","Grammatik","Sprechen",
            "Hebdo (40%)","Endprüfung (60%)","Total (%)"
        );
        return t;
    }

    private PdfPTable creerTableA2() {
        PdfPTable t = new PdfPTable(10);
        t.setWidthPercentage(100);
        headers(t,
            "Élève",
            "Lesen","Hören","Schreiben","Grammatik","Sprechen",
            "Hebdo (40%)","Endprüfung (50%)","Soutenance","Total (%)"
        );
        return t;
    }

    private PdfPTable creerTableEndprufung() {
        PdfPTable t = new PdfPTable(6);
        t.setWidthPercentage(100);
        headers(t,
            "Élève",
            "Lesen","Hören","Schreiben","Grammatik","Sprechen"
        );
        return t;
    }

    private PdfPTable creerTableResultatsB1B2(boolean avecSoutenance) {
        PdfPTable t = new PdfPTable(avecSoutenance ? 6 : 5);
        t.setWidthPercentage(100);

        if (avecSoutenance) {
            headers(t,
                "Élève",
                "Hebdo (40%)",
                "Endprüfung (60%)",
                "Soutenance",
                "Total (%)",
                "Décision"
            );
        } else {
            headers(t,
                "Élève",
                "Hebdo (40%)",
                "Endprüfung (60%)",
                "Total (%)",
                "Décision"
            );
        }
        return t;
    }

    // =========================================================
    // ======================= LIGNES ==========================
    // =========================================================

    private void ajouterLigneA1(PdfPTable t, Resultat r) {
        t.addCell(cellNom(r));
        ajouterNotesEnd(t, r);
        t.addCell(cellPercent(hebdoPct(r)));
        t.addCell(cellPercent(endPct(r)));
        t.addCell(cellPercent(r.getMoyenneGenerale()));
    }

    private void ajouterLigneA2(PdfPTable t, Resultat r) {
        t.addCell(cellNom(r));
        ajouterNotesEnd(t, r);
        t.addCell(cellPercent(hebdoPct(r)));
        t.addCell(cellPercent(endPct(r)));
        t.addCell(noteCell(r.getSoutenance()));
        t.addCell(cellPercent(r.getMoyenneGenerale()));
    }

    private void ajouterLigneEndprufung(PdfPTable t, Resultat r) {
        t.addCell(cellNom(r));
        ajouterNotesEnd(t, r);
    }

    private void ajouterLigneResultatsB1B2(
        PdfPTable t,
        Resultat r,
        boolean avecSoutenance
    ) {
        t.addCell(cellNom(r));
        t.addCell(cellPercent(hebdoPct(r)));
        t.addCell(cellPercent(endPct(r)));

        if (avecSoutenance) {
            t.addCell(noteCell(r.getSoutenance()));
        }

        t.addCell(cellTotal(r));      // ✅ couleur
        t.addCell(cellDecision(r));   // ✅ B1 / B2 uniquement
    }

    private void ajouterNotesEnd(PdfPTable t, Resultat r) {
        t.addCell(noteCell(r.getEndLes()));
        t.addCell(noteCell(r.getEndHor()));
        t.addCell(noteCell(r.getEndSchreib()));
        t.addCell(noteCell(r.getEndGramm()));
        t.addCell(noteCell(r.getEndSpre()));
    }

    // =========================================================
    // ======================= CALCULS =========================
    // =========================================================

    private Double moyHebdo(Resultat r) {
        Double[] v = {
            r.getMoyLes(), r.getMoyHor(),
            r.getMoySchreib(), r.getMoyGramm(), r.getMoySpre()
        };
        return Stream.of(v)
            .filter(x -> x != null)
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0);
    }

    private Double hebdoPct(Resultat r) {
        return (moyHebdo(r) / 20.0) * 40.0;
    }

    private Double endPct(Resultat r) {
        Double[] v = {
            r.getEndLes(), r.getEndHor(),
            r.getEndSchreib(), r.getEndGramm(), r.getEndSpre()
        };
        double moy = Stream.of(v)
            .filter(x -> x != null)
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0);
        return (moy / 20.0) * 60.0;
    }

    // =========================================================
    // ======================= UTILS ===========================
    // =========================================================

    private PdfPCell noteCell(Double v) {
        PdfPCell c = new PdfPCell(new Phrase(val(v)));
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        if (v != null) {
            c.setBackgroundColor(
                v >= 12 ? new Color(210,255,210) : new Color(255,210,210)
            );
        }
        return c;
    }

    private PdfPCell cellNom(Resultat r) {
        return new PdfPCell(
            new Phrase(r.getEleve().getNom() + " " + r.getEleve().getPrenom())
        );
    }

    private PdfPCell cellPercent(Double v) {
        PdfPCell c = new PdfPCell(
            new Phrase(v == null ? "-" : String.format("%.2f %%", v))
        );
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        return c;
    }

    private PdfPCell cellTotal(Resultat r) {
        Double v = r.getMoyenneGenerale();
        PdfPCell c = new PdfPCell(
            new Phrase(v == null ? "-" : String.format("%.2f %%", v))
        );
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        
        if (v != null) {
            c.setBackgroundColor(v >= 60 ? new Color(200,255,200) : new Color(255,200,200));
        }
        return c;
    }

    private PdfPCell cellDecision(Resultat r) {
        PdfPCell c = new PdfPCell(
            new Phrase(r.isAdmis() ? "ADMIS" : "AJOURNÉ")
        );
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        return c;
    }

    private String val(Double d) {
        return d == null ? "-" : String.format("%.2f", d);
    }

    private void headers(PdfPTable t, String... h) {
        for (String x : h) {
            PdfPCell c = new PdfPCell(
                new Phrase(x, FontFactory.getFont(FontFactory.HELVETICA_BOLD))
            );
            c.setBackgroundColor(Color.LIGHT_GRAY);
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            t.addCell(c);
        }
    }

    private Paragraph sectionTitle(String t) {
        Paragraph p = new Paragraph(
            t, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)
        );
        p.setSpacingAfter(6);
        return p;
    }

    // =========================================================
    // =================== STATS ===============================
    // =========================================================

    private void ajouterStatistiquesClasse(Document doc, List<Resultat> rs)
        throws DocumentException {

        double moyClasse = rs.stream()
            .map(Resultat::getMoyenneGenerale)
            .filter(x -> x != null)
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0);

        Resultat premier = rs.stream()
            .filter(r -> r.getMoyenneGenerale() != null)
            .max(Comparator.comparing(Resultat::getMoyenneGenerale))
            .orElse(null);

        doc.add(new Paragraph(
            "Moyenne de la classe : " + String.format("%.2f %%", moyClasse)
        ));

        if (premier != null) {
            doc.add(new Paragraph(
                "Premier de la classe : " +
                premier.getEleve().getNom() + " " +
                premier.getEleve().getPrenom() +
                " (" + String.format("%.2f %%", premier.getMoyenneGenerale()) + ")"
            ));
        }
    }

    // =========================================================
    // ======================= SIGNATURES ======================
    // =========================================================

    private void ajouterSignatures(Document doc) throws DocumentException {
        doc.add(Chunk.NEWLINE);
        PdfPTable t = new PdfPTable(2);
        t.setWidthPercentage(100);

        PdfPCell c1 = new PdfPCell(
            new Phrase("Responsable pédagogique\n\nSignature : ______________")
        );
        PdfPCell c2 = new PdfPCell(
            new Phrase("Direction FLCS\n\nSignature : ______________")
        );

        c1.setBorder(Rectangle.NO_BORDER);
        c2.setBorder(Rectangle.NO_BORDER);
        c2.setHorizontalAlignment(Element.ALIGN_RIGHT);

        t.addCell(c1);
        t.addCell(c2);
        doc.add(t);
    }
}

