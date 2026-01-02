package FLCS.GESTION.EXPORT;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import FLCS.GESTION.DTO.PaiementExport;
import FLCS.GESTION.DTO.PaiementHistoriqueResponse;

import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

@Component
public class PaiementPdfGenerator {

    public byte[] generate(PaiementExport dto) {

        Document document = new Document(PageSize.A4);
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


            // ===== TITRE =====
            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Reçu de Paiement – FLCS", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" "));

            // ===== INFOS ELEVE =====
            document.add(new Paragraph("Élève : " + dto.nomEleve()));
            document.add(new Paragraph("Montant total : " + dto.montantTotal() + " FCFA"));
            document.add(new Paragraph("Total payé : " + dto.totalPaye() + " FCFA"));
            document.add(new Paragraph("Reste à payer : " + dto.resteAPayer() + " FCFA"));

            document.add(new Paragraph(" "));

            // ===== TABLE HISTORIQUE =====
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);

            table.addCell("Date");
            table.addCell("Montant");
            table.addCell("Référence");

            for (PaiementHistoriqueResponse p : dto.historique()) {
                table.addCell(p.datePaiement().toString());
                table.addCell(p.montant() + " FCFA");
                table.addCell(p.referenceVirement());
            }

            document.add(table);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Signature FLCS"));

            document.close();

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du PDF", e);
        }

        return out.toByteArray();
    }
}
