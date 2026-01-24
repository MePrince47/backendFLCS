package FLCS.GESTION.EXPORT;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import FLCS.GESTION.ENTITEES.Eleve;

import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.awt.Color;

@Component
public class ElevePdfExporter {

    public byte[] exporter(List<Eleve> eleves, String titreDoc) {

        if (eleves.isEmpty()) {
            throw new IllegalStateException("Aucun élève à exporter");
        }

        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // ===== TITRE =====
            Paragraph titre = new Paragraph(
                titreDoc,
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)
            );
            titre.setAlignment(Element.ALIGN_CENTER);
            document.add(titre);
            document.add(Chunk.NEWLINE);

            // ===== TABLE =====
            PdfPTable table = new PdfPTable(9);
            table.setWidthPercentage(100);

            headers(table,
                "Nom",
                "Prénom",
                "Date Naiss.",
                "Téléphone",
                "Niveau langue",
                "Niveau scolaire",
                "Rentrée",
                "Partenaire",
                "Procédure"
            );

            for (Eleve e : eleves) {
                table.addCell(val(e.getNom()));
                table.addCell(val(e.getPrenom()));
                table.addCell(val(e.getDateNaiss()));
                table.addCell(val(e.getTelCandidat()));
                table.addCell(
                    e.getNiveauLangue() != null
                        ? e.getNiveauLangue().getCode()
                        : "-"
                );
                table.addCell(val(e.getNiveauScolaire()));
                table.addCell(
                    e.getRentree() != null
                        ? e.getRentree().getNomRentree()
                        : "-"
                );
                table.addCell(
                    e.getPartenaire() != null
                        ? e.getPartenaire().getNomPartenaire()
                        : "-"
                );
                table.addCell(val(e.getTypeProcedure()));
            }

            document.add(table);
            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erreur génération PDF élèves", e);
        }
    }

    // ================= UTILS =================

    private void headers(PdfPTable table, String... titles) {
        for (String t : titles) {
            PdfPCell c = new PdfPCell(
                new Phrase(t, FontFactory.getFont(FontFactory.HELVETICA_BOLD))
            );
            c.setBackgroundColor(Color.LIGHT_GRAY);
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c);
        }
    }

    private String val(Object o) {
        return o == null ? "-" : o.toString();
    }
}
