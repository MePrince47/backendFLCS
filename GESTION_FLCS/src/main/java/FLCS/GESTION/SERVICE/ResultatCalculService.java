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

        // ================= NOTES HEBDO =================
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

        double moyHebdo = (hLes + hHor + hSch + hGra + hSpr) / 5;

        // ================= ENDPRÜFUNG =================
        NoteEndprufung end =
            noteEndRepo.findByEleve_IdAndEndprufung_Niveau_Id(eleveId, niveauId)
                .orElse(null);

        Double eLes = end != null ? end.getLes() : null;
        Double eHor = end != null ? end.getHor() : null;
        Double eSch = end != null ? end.getSchreib() : null;
        Double eGra = end != null ? end.getGramm() : null;
        Double eSpr = end != null ? end.getSpre() : null;

        double moyEnd = end != null
            ? moyenne(List.of(eLes, eHor, eSch, eGra, eSpr))
            : 0;

        // ================= SOUTENANCE (A2 / B2) =================
        Double soutenance =
            (code.equals("A2") || code.equals("B2"))
                ? soutenanceRepo.findByEleve_IdAndNiveau_Id(eleveId, niveauId)
                    .map(s -> s.getNote()) // SUR 20
                    .orElse(null)
                : null;

        // ======================= A1 =======================
        if (code.equals("A1")) {

            double moyenneGenerale =
                (moyHebdo * 0.6) +
                (moyEnd * 0.4);

            return new ResultatCalculDTO(
                eleveId, niveauId,
                hLes, hHor, hSch, hGra, hSpr,    // ON STOCKE LES MOY. HEBDO
                eLes, eHor, eSch, eGra, eSpr,
                null,
                moyenneGenerale,
                moyenneGenerale >= niveau.getBareme()
            );

        }

        // ======================= A2 =======================
        if (code.equals("A2")) {

            double moyenneGenerale =
                (moyEnd * 0.5) +
                (moyHebdo * 0.3) +
                (soutenance != null ? soutenance * 0.2 : 0);

            return new ResultatCalculDTO(
                eleveId, niveauId,
                hLes, hHor, hSch, hGra, hSpr,    // ON STOCKE LES MOY. HEBDO
                eLes, eHor, eSch, eGra, eSpr,
                null,
                moyenneGenerale,
                moyenneGenerale >= niveau.getBareme()
            );

        }

        // ======================= B1 / B2 =======================
        double mLes = moyenneFinale(hLes, end, NoteEndprufung::getLes, 0.4, 0.6);
        double mHor = moyenneFinale(hHor, end, NoteEndprufung::getHor, 0.4, 0.6);
        double mSch = moyenneFinale(hSch, end, NoteEndprufung::getSchreib, 0.4, 0.6);
        double mGra = moyenneFinale(hGra, end, NoteEndprufung::getGramm, 0.4, 0.6);
        double mSpr = moyenneFinale(hSpr, end, NoteEndprufung::getSpre, 0.4, 0.6);

        double seuil = niveau.getBareme() * 0.6;

        boolean admis =
            mLes >= seuil &&
            mHor >= seuil &&
            mSch >= seuil &&
            mGra >= seuil &&
            mSpr >= seuil;

        return new ResultatCalculDTO(
            eleveId, niveauId,
            mLes, mHor, mSch, mGra, mSpr,
            eLes, eHor, eSch, eGra, eSpr,
            soutenance,   // seulement affichée pour B2
            null,         // PAS de moyenne générale
            admis
        );
    }

    // ================= UTILITAIRES =================

    private double moyenne(List<Double> valeurs) {
        return valeurs.stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0);
    }

    private double moyenneFinale(
        double hebdo,
        NoteEndprufung end,
        java.util.function.Function<NoteEndprufung, Double> getter,
        double cHebdo,
        double cEnd
    ) {
        if (end == null) return hebdo;
        return (hebdo * cHebdo) + (getter.apply(end) * cEnd);
    }
}


