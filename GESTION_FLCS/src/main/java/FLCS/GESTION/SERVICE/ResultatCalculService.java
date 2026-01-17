package FLCS.GESTION.SERVICE;

import FLCS.GESTION.ENTITEES.NoteHebdo;
import FLCS.GESTION.ENTITEES.NoteEndprufung;
import FLCS.GESTION.ENTITEES.Niveau;

import FLCS.GESTION.DTO.ResultatCalculDTO;
import FLCS.GESTION.REPOSITORY.NoteHebdoRepository;
import FLCS.GESTION.REPOSITORY.NoteEndprufungRepository;
import FLCS.GESTION.REPOSITORY.NiveauRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ResultatCalculService {

    private final NoteHebdoRepository noteHebdoRepo;
    private final NoteEndprufungRepository noteEndRepo;
    private final NiveauRepository niveauRepo;

    public ResultatCalculService(
        NoteHebdoRepository noteHebdoRepo,
        NoteEndprufungRepository noteEndRepo,
        NiveauRepository niveauRepo
    ) {
        this.noteHebdoRepo = noteHebdoRepo;
        this.noteEndRepo = noteEndRepo;
        this.niveauRepo = niveauRepo;
    }

    public ResultatCalculDTO calculerPourEleve(Long eleveId, Long niveauId) {

        //  Notes hebdomadaires
        List<NoteHebdo> hebdos =
            noteHebdoRepo.findByEleve_IdAndEvaluationHebdo_Niveau_Id(
                eleveId, niveauId
            );

        if (hebdos.size() < 7) {
            throw new IllegalStateException(
                "Notes hebdomadaires incomplètes (7 semaines requises)"
            );
        }

        //  Endprufung
        NoteEndprufung endprufung =
            noteEndRepo.findByEleve_IdAndEndprufung_Niveau_Id(eleveId, niveauId)
                .orElse(null);

        //  Moyennes hebdo
        double moyLesHebdo = moyenne(hebdos.stream().map(NoteHebdo::getLes).toList());
        double moyHorHebdo = moyenne(hebdos.stream().map(NoteHebdo::getHor).toList());
        double moySchreibHebdo = moyenne(hebdos.stream().map(NoteHebdo::getSchreib).toList());
        double moyGrammHebdo = moyenne(hebdos.stream().map(NoteHebdo::getGramm).toList());
        double moySpreHebdo = moyenne(hebdos.stream().map(NoteHebdo::getSpre).toList());

        // Moyennes finales (pondération)
        double coeffHebdo = 0.6;
        double coeffEnd = 0.4;

        double moyLes = moyenneFinale(moyLesHebdo, endprufung, NoteEndprufung::getLes, coeffHebdo, coeffEnd);
        double moyHor = moyenneFinale(moyHorHebdo, endprufung, NoteEndprufung::getHor, coeffHebdo, coeffEnd);
        double moySchreib = moyenneFinale(moySchreibHebdo, endprufung, NoteEndprufung::getSchreib, coeffHebdo, coeffEnd);
        double moyGramm = moyenneFinale(moyGrammHebdo, endprufung, NoteEndprufung::getGramm, coeffHebdo, coeffEnd);
        double moySpre = moyenneFinale(moySpreHebdo, endprufung, NoteEndprufung::getSpre, coeffHebdo, coeffEnd);

        double noteEnd = endprufung != null
            ? moyenne(List.of(
                endprufung.getLes(),
                endprufung.getHor(),
                endprufung.getSchreib(),
                endprufung.getGramm(),
                endprufung.getSpre()
            ))
            : null;

        // Notes Endprufung
        double endLes = endprufung != null ? endprufung.getLes() : null;
        double endHor = endprufung != null ? endprufung.getHor() : null;
        double endSchreib = endprufung != null ? endprufung.getSchreib() : null;
        double endGramm = endprufung != null ? endprufung.getGramm() : null;
        double endSpre = endprufung != null ? endprufung.getSpre() : null;

        // Moyenne générale
        double moyenneGenerale =
            (moyLes + moyHor + moySchreib + moyGramm + moySpre) / 5;

        // Admission
        Niveau niveau =
            niveauRepo.findById(niveauId)
                .orElseThrow(() -> new IllegalArgumentException("Niveau introuvable"));

        double seuil = niveau.getBareme() * 0.6;
        boolean admis =
            moyLes >= seuil &&
            moyHor >= seuil &&
            moySchreib >= seuil &&
            moyGramm >= seuil &&
            moySpre >= seuil;

        return new ResultatCalculDTO(
            eleveId,
            niveauId,

            moyLes,
            moyHor,
            moySchreib,
            moyGramm,
            moySpre,

            endLes,
            endHor,
            endSchreib,
            endGramm,
            endSpre,

            moyenneGenerale,
            admis
        );

    }

    // ======= UTILITAIRES =======

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
        if (end == null) {
            return hebdo; // pas d'endprufung = contrôle continu seul
        }
        return (hebdo * cHebdo) + (getter.apply(end) * cEnd);
    }
}
