package FLCS.GESTION.SERVICE;

import FLCS.GESTION.ENTITEES.*;
import FLCS.GESTION.DTO.ResultatCalculDTO;
import FLCS.GESTION.DTO.ResultatResponse;
import FLCS.GESTION.REPOSITORY.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ResultatService {

    private final ResultatRepository resultatRepo;
    private final ResultatCalculService calculService;
    private final EleveRepository eleveRepo;
    private final NiveauRepository niveauRepo;

    public ResultatService(
        ResultatRepository resultatRepo,
        ResultatCalculService calculService,
        EleveRepository eleveRepo,
        NiveauRepository niveauRepo
    ) {
        this.resultatRepo = resultatRepo;
        this.calculService = calculService;
        this.eleveRepo = eleveRepo;
        this.niveauRepo = niveauRepo;
    }

    /**
     * Générer le résultat FINAL d’un élève pour un niveau
     */
    public ResultatResponse genererPourEleve(Long eleveId, Long niveauId) {

        if (resultatRepo.existsByEleve_IdAndNiveau_Id(eleveId, niveauId)) {
            throw new IllegalStateException("Résultat déjà généré");
        }

        ResultatCalculDTO calc =
            calculService.calculerPourEleve(eleveId, niveauId);

        Eleve eleve = eleveRepo.findById(eleveId)
            .orElseThrow(() -> new IllegalArgumentException("Élève introuvable"));

        Niveau niveau = niveauRepo.findById(niveauId)
            .orElseThrow(() -> new IllegalArgumentException("Niveau introuvable"));

        Resultat resultat = Resultat.builder()
            .eleve(eleve)
            .niveau(niveau)

            // moyennes finales
            .moyLes(calc.moyLes())
            .moyHor(calc.moyHor())
            .moySchreib(calc.moySchreib())
            .moyGramm(calc.moyGramm())
            .moySpre(calc.moySpre())

            // notes endprüfung (AFFICHAGE SEULEMENT)
            .endLes(calc.endLes())
            .endHor(calc.endHor())
            .endSchreib(calc.endSchreib())
            .endGramm(calc.endGramm())
            .endSpre(calc.endSpre())

            .moyenneGenerale(calc.moyenneGenerale())
            .admis(calc.admis())
            .build();

        return toResponse(resultatRepo.save(resultat));
    }

    /**
     * Générer les résultats de TOUTE une classe
     */
    public List<ResultatResponse> genererPourNiveau(Long niveauId) {

        niveauRepo.findById(niveauId)
            .orElseThrow(() -> new IllegalArgumentException("Niveau introuvable"));

        List<Eleve> eleves =
            eleveRepo.findByNiveauLangue_Id(niveauId);

        return eleves.stream()
            .map(e -> genererPourEleve(e.getId(), niveauId))
            .toList();
    }

    // ===== DTO mapping =====
    private ResultatResponse toResponse(Resultat r) {
        return new ResultatResponse(
            r.getId(),
            r.getEleve().getNom() + " " + r.getEleve().getPrenom(),

            r.getMoyLes(),
            r.getMoyHor(),
            r.getMoySchreib(),
            r.getMoyGramm(),
            r.getMoySpre(),

            r.getEndLes(),
            r.getEndHor(),
            r.getEndSchreib(),
            r.getEndGramm(),
            r.getEndSpre(),

            r.getMoyenneGenerale(),
            r.isAdmis()
        );
    }
}