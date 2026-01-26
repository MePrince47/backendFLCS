package FLCS.GESTION.SERVICE;

import FLCS.GESTION.ENTITEES.NoteHebdo;
import FLCS.GESTION.ENTITEES.NoteEndprufung;
import FLCS.GESTION.ENTITEES.Niveau;

import FLCS.GESTION.DTO.ResultatCalculDTO;
import FLCS.GESTION.REPOSITORY.NoteHebdoRepository;
import FLCS.GESTION.REPOSITORY.NoteEndprufungRepository;
import FLCS.GESTION.REPOSITORY.NoteSoutenanceRepository;
import FLCS.GESTION.REPOSITORY.NiveauRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

@Service
@Transactional(readOnly = true)
public class ResultatCalculService {

    private final NoteHebdoRepository noteHebdoRepo;
    private final NoteEndprufungRepository noteEndRepo;
    private final NoteSoutenanceRepository soutenanceRepo;
    private final NiveauRepository niveauRepo;

    public ResultatCalculService(
        NoteHebdoRepository noteHebdoRepo,
        NoteEndprufungRepository noteEndRepo,
        NoteSoutenanceRepository soutenanceRepo,
        NiveauRepository niveauRepo
    ) {
        this.noteHebdoRepo = noteHebdoRepo;
        this.noteEndRepo = noteEndRepo;
        this.soutenanceRepo = soutenanceRepo;
        this.niveauRepo = niveauRepo;
    }

    public ResultatCalculDTO calculerPourEleve(Long eleveId, Long niveauId) {

        Niveau niveau = niveauRepo.findById(niveauId)
            .orElseThrow(() -> new IllegalArgumentException("Niveau introuvable"));

        String code = niveau.getCode().split("_")[0]; // A1_SEPT → A1

        /* ================= NOTES HEBDO ================= */
        List<NoteHebdo> hebdos =
            noteHebdoRepo.findByEleve_IdAndEvaluationHebdo_Niveau_Id(eleveId, niveauId);

        if (hebdos.size() < 7) {
            throw new IllegalStateException("7 notes hebdomadaires requises");
        }

        double hLes = moyenne(hebdos.stream().map(NoteHebdo::getLes).toList());
        double hHor = moyenne(hebdos.stream().map(NoteHebdo::getHor).toList());
        double hSch = moyenne(hebdos.stream().map(NoteHebdo::getSchreib).toList());
        double hGra = moyenne(hebdos.stream().map(NoteHebdo::getGramm).toList());
        double hSpr = moyenne(hebdos.stream().map(NoteHebdo::getSpre).toList());

        double moyHebdo20 = moyenne(List.of(hLes, hHor, hSch, hGra, hSpr)); // /20

        /* ================= ENDPRÜFUNG ================= */
        NoteEndprufung end =
            noteEndRepo.findByEleve_IdAndEndprufung_Niveau_Id(eleveId, niveauId)
                .orElse(null);

        Double eLes = end != null ? end.getLes() : null;
        Double eHor = end != null ? end.getHor() : null;
        Double eSch = end != null ? end.getSchreib() : null;
        Double eGra = end != null ? end.getGramm() : null;
        Double eSpr = end != null ? end.getSpre() : null;

        double moyEnd20 = end != null
            ? moyenne(List.of(eLes, eHor, eSch, eGra, eSpr))
            : 0;

        /* ================= SOUTENANCE ================= */
        Double soutenance =
            (code.equals("A2") || code.equals("B2"))
                ? soutenanceRepo.findByEleve_IdAndNiveau_Id(eleveId, niveauId)
                    .map(s -> s.getNote()) // sur 20
                    .orElse(null)
                : null;

        /* ======================= A1 ======================= */
        if (code.equals("A1")) {

            double hebdoPct = moyHebdo20 * 2; // 40 %
            double endPct   = moyEnd20 * 3;   // 60 %
            double total    = hebdoPct + endPct;

            return new ResultatCalculDTO(
                eleveId, niveauId,
                hLes, hHor, hSch, hGra, hSpr,
                eLes, eHor, eSch, eGra, eSpr,
                null,
                total,
                total >= niveau.getBareme()
            );
        }

        /* ======================= A2 ======================= */
        if (code.equals("A2")) {

            double hebdoPct = moyHebdo20 * 1.5; // 30 %
            double endPct   = moyEnd20 * 2.5;   // 50 %
            double soutPct  = soutenance != null ? soutenance : 0; // 20 %

            double total = hebdoPct + endPct + soutPct;

            return new ResultatCalculDTO(
                eleveId, niveauId,
                hLes, hHor, hSch, hGra, hSpr,
                eLes, eHor, eSch, eGra, eSpr,
                soutenance,
                total,
                total >= niveau.getBareme()
            );
        }

        /* ======================= B1 / B2 ======================= */
        double mLes = moyenneFinale(hLes, end, NoteEndprufung::getLes);
        double mHor = moyenneFinale(hHor, end, NoteEndprufung::getHor);
        double mSch = moyenneFinale(hSch, end, NoteEndprufung::getSchreib);
        double mGra = moyenneFinale(hGra, end, NoteEndprufung::getGramm);
        double mSpr = moyenneFinale(hSpr, end, NoteEndprufung::getSpre);

        boolean admis =
            mLes >= 12 &&
            mHor >= 12 &&
            mSch >= 12 &&
            mGra >= 12 &&
            mSpr >= 12;

        return new ResultatCalculDTO(
            eleveId, niveauId,
            mLes, mHor, mSch, mGra, mSpr,
            eLes, eHor, eSch, eGra, eSpr,
            soutenance,
            null,
            admis
        );
    }

    /* ================= UTILITAIRES ================= */

    private double moyenne(List<Double> valeurs) {
        return valeurs.stream()
            .filter(v -> v != null)
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0);
    }

    private double moyenneFinale(
        double hebdo,
        NoteEndprufung end,
        Function<NoteEndprufung, Double> getter
    ) {
        if (end == null) return hebdo;
        return (hebdo * 0.4) + (getter.apply(end) * 0.6);
    }
}
