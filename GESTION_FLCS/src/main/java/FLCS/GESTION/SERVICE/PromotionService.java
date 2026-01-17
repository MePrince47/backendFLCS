package FLCS.GESTION.SERVICE;

import FLCS.GESTION.ENTITEES.Eleve;
import FLCS.GESTION.ENTITEES.Niveau;
import FLCS.GESTION.ENTITEES.Resultat;

import FLCS.GESTION.REPOSITORY.EleveRepository;
import FLCS.GESTION.REPOSITORY.NiveauRepository;
import FLCS.GESTION.REPOSITORY.ResultatRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class PromotionService {

    private final NiveauRepository niveauRepo;
    private final ResultatRepository resultatRepo;
    private final EleveRepository eleveRepo;

    public PromotionService(
        NiveauRepository niveauRepo,
        ResultatRepository resultatRepo,
        EleveRepository eleveRepo
    ) {
        this.niveauRepo = niveauRepo;
        this.resultatRepo = resultatRepo;
        this.eleveRepo = eleveRepo;
    }

    public void promouvoirNiveau(Long niveauId) {

        Niveau niveau = niveauRepo.findById(niveauId)
            .orElseThrow(() -> new IllegalArgumentException("Niveau introuvable"));

        if (!niveau.isCloture()) {
            throw new IllegalStateException("Niveau non clôturé");
        }

        Niveau niveauSuivant =
            niveauRepo.findByCode(codeSuivant(niveau.getCode()))
                .orElseThrow(() ->
                    new IllegalStateException("Niveau suivant introuvable")
                );

        List<Resultat> resultats =
            resultatRepo.findByNiveau_Id(niveauId);

        for (Resultat r : resultats) {
            if (r.isAdmis()) {
                Eleve e = r.getEleve();
                e.setNiveauLangue(niveauSuivant);
                eleveRepo.save(e);
            }
        }
    }

    private String codeSuivant(String code) {
        return switch (code) {
            case "A1" -> "A2";
            case "A2" -> "B1";
            case "B1" -> "B2";
            default -> throw new IllegalStateException("Niveau final atteint");
        };
    }
}
