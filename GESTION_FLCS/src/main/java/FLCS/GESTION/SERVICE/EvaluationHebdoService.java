package FLCS.GESTION.SERVICE;

import FLCS.GESTION.ENTITEES.EvaluationHebdo;
import FLCS.GESTION.ENTITEES.Niveau;

import FLCS.GESTION.REPOSITORY.EvaluationHebdoRepository;
import FLCS.GESTION.REPOSITORY.NiveauRepository;

import FLCS.GESTION.DTO.NoteResponseDTO;
import FLCS.GESTION.DTO.EvaluationHebdoResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EvaluationHebdoService {

    private final EvaluationHebdoRepository evalRepo;
    private final NiveauRepository niveauRepo;

    public EvaluationHebdoService(
            EvaluationHebdoRepository evalRepo,
            NiveauRepository niveauRepo
    ) {
        this.evalRepo = evalRepo;
        this.niveauRepo = niveauRepo;
    }

    /**
     * Création automatique des 7 évaluations hebdomadaires pour un niveau
     */
    public void creerEvaluationsPourNiveau(Long niveauId) {

        Niveau niveau = niveauRepo.findById(niveauId)
                .orElseThrow(() -> new IllegalArgumentException("Niveau introuvable"));

        if (evalRepo.existsByNiveau(niveau)) {
            throw new IllegalStateException(
                    "Les évaluations hebdomadaires existent déjà pour ce niveau"
            );
        }

        for (int semaine = 1; semaine <= 7; semaine++) {
            evalRepo.save(
                    EvaluationHebdo.builder()
                            .niveau(niveau)
                            .semaineNum(semaine)
                            .build()
            );
        }
    }

    /**
     * Liste des évaluations d’un niveau (triées)
     */
    @Transactional(readOnly = true)
    public List<EvaluationHebdoResponse> getResponsesByNiveau(Long niveauId) {

        Niveau niveau = niveauRepo.findById(niveauId)
                .orElseThrow(() -> new IllegalArgumentException("Niveau introuvable"));

        return evalRepo.findByNiveauOrderBySemaineNumAsc(niveau)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private EvaluationHebdoResponse toResponse(EvaluationHebdo e) {

        List<NoteResponseDTO> notes =
            e.getNotes() == null
                ? List.of()
                : e.getNotes().stream()
                    .map(n -> new NoteResponseDTO(
                        n.getEleve().getId(),
                        n.getEleve().getNom() + " " + n.getEleve().getPrenom(),
                        n.getLes(),
                        n.getHor(),
                        n.getSchreib(),
                        n.getGramm(),
                        n.getSpre()
                    ))
                    .toList();

        return new EvaluationHebdoResponse(
            e.getId(),
            e.getSemaineNum(),
            notes
        );
    }

    public boolean existePourNiveau(Long niveauId) {

        Niveau niveau = niveauRepo.findById(niveauId)
            .orElseThrow(() ->
                new IllegalArgumentException("Niveau introuvable")
            );

        return evalRepo.existsByNiveau(niveau);
    }

}
