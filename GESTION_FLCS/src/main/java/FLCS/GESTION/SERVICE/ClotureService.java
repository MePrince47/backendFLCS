package FLCS.GESTION.SERVICE;

import FLCS.GESTION.ENTITEES.Niveau;
import FLCS.GESTION.ENTITEES.Eleve;

import FLCS.GESTION.REPOSITORY.NiveauRepository;
import FLCS.GESTION.REPOSITORY.ResultatRepository;
import FLCS.GESTION.REPOSITORY.EleveRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class ClotureService {

    private final NiveauRepository niveauRepo;
    private final ResultatRepository resultatRepo;
    private final EleveRepository eleveRepo;

    public ClotureService(
        NiveauRepository niveauRepo,
        ResultatRepository resultatRepo,
        EleveRepository eleveRepo
    ) {
        this.niveauRepo = niveauRepo;
        this.resultatRepo = resultatRepo;
        this.eleveRepo = eleveRepo;
    }

    public void cloturerNiveau(Long niveauId) {

        Niveau niveau = niveauRepo.findById(niveauId)
            .orElseThrow(() -> new IllegalArgumentException("Niveau introuvable"));

        if (niveau.isCloture()) {
            throw new IllegalStateException("Niveau déjà clôturé");
        }

        List<Eleve> eleves =
            eleveRepo.findByNiveauLangue_Id(niveauId);

        boolean tousResultatsGeneres = eleves.stream()
            .allMatch(e ->
                resultatRepo.existsByEleve_IdAndNiveau_Id(
                    e.getId(), niveauId
                )
            );

        if (!tousResultatsGeneres) {
            throw new IllegalStateException(
                "Tous les résultats ne sont pas encore générés"
            );
        }

        niveau.setCloture(true);
        niveauRepo.save(niveau);
    }
}
