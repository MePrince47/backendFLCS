package FLCS.GESTION.EXPORT;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import FLCS.GESTION.DTO.*;
import FLCS.GESTION.ENTITEES.TypeNote;


import org.springframework.stereotype.Component;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.util.Comparator;

@Component
public class BulletinPdfExporter {

    public byte[] generer(BulletinDTO bulletin) {

        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // ===== TITRE =====
            Paragraph titre = new Paragraph(
                "BULLETIN OFFICIEL",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)
            );
            titre.setAlignment(Element.ALIGN_CENTER);
            document.add(titre);

            document.add(Chunk.NEWLINE);

            // ===== INFOS ÉLÈVE =====
            EleveResponse e = bulletin.eleve();

            document.add(new Paragraph(
                "Élève : " + e.nom() + " " + e.prenom()));
            document.add(new Paragraph(
                "Niveau : " + bulletin.niveau()));
            document.add(new Paragraph(
                "Rentrée : " + bulletin.rentree()));

            document.add(Chunk.NEWLINE);

            // ===== NOTES HEBDO =====
            document.add(section("NOTES HEBDOMADAIRES"));

            PdfPTable hebdo = new PdfPTable(6);
            hebdo.setWidthPercentage(100);

            headers(hebdo,
                "Semaine", "Lesen", "Hören",
                "Schreiben", "Grammatik", "Sprechen"
            );

            bulletin.notes().stream()
                .filter(n -> n.type() == TypeNote.HEBDO)
                .sorted(Comparator.comparing(NoteResponse::semaine))
                .forEach(n -> {
                    hebdo.addCell(String.valueOf(n.semaine()));
                    hebdo.addCell(val(n.les()));
                    hebdo.addCell(val(n.hor()));
                    hebdo.addCell(val(n.schreib()));
                    hebdo.addCell(val(n.gramm()));
                    hebdo.addCell(val(n.spre()));
                });

            document.add(hebdo);
            document.add(Chunk.NEWLINE);

            // ===== ENDPRÜFUNG =====
            bulletin.notes().stream()
                .filter(n -> n.type() == TypeNote.ENDPRUFUNG)
                .findFirst()
                .ifPresent(n -> {
                    try {
                        document.add(section("ENDPRÜFUNG"));

                        PdfPTable end = new PdfPTable(5);
                        end.setWidthPercentage(100);

                        headers(end,
                            "Lesen", "Hören",
                            "Schreiben", "Grammatik", "Sprechen"
                        );

                        end.addCell(val(n.les()));
                        end.addCell(val(n.hor()));
                        end.addCell(val(n.schreib()));
                        end.addCell(val(n.gramm()));
                        end.addCell(val(n.spre()));

                        document.add(end);
                        document.add(Chunk.NEWLINE);

                    } catch (DocumentException ex) {
                        throw new RuntimeException(ex);
                    }
                });

            // ===== SOUTENANCE =====
            if (bulletin.soutenance() != null) {
                document.add(section("SOUTENANCE"));
                document.add(new Paragraph(
                    "Note : " + String.format("%.2f / 20", bulletin.soutenance())
                ));
                document.add(Chunk.NEWLINE);
            }

            // ===== RÉSULTAT FINAL =====
            document.add(section("RÉSULTAT FINAL"));

            document.add(new Paragraph(
                "Moyenne générale : " +
                String.format("%.2f %%", bulletin.moyenneGenerale())
            ));

            Paragraph decision = new Paragraph(
                bulletin.admis() ? "ADMIS" : "AJOURNÉ",
                FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD,
                    14,
                    bulletin.admis() ? Color.GREEN : Color.RED
                )
            );
            document.add(decision);

            document.close();
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erreur génération bulletin PDF", e);
        }
    }

    // ================= UTILS =================

    private Paragraph section(String t) {
        Paragraph p = new Paragraph(
            t,
            FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)
        );
        p.setSpacingAfter(5);
        return p;
    }

    private void headers(PdfPTable table, String... titles) {
        for (String h : titles) {
            PdfPCell c = new PdfPCell(
                new Phrase(h,
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD))
            );
            c.setBackgroundColor(Color.LIGHT_GRAY);
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c);
        }
    }

    private String val(Double d) {
        return d == null ? "-" : String.format("%.2f", d);
    }
}
