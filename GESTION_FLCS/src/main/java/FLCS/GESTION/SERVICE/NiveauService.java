package FLCS.GESTION.SERVICE;

import FLCS.GESTION.ENTITEES.Niveau;
import FLCS.GESTION.REPOSITORY.NiveauRepository;
import FLCS.GESTION.DTO.NiveauResponse;
import FLCS.GESTION.DTO.EvaluationHebdoResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NiveauService {

    private final NiveauRepository niveauRepo;
    private final EvaluationHebdoService evalService;

    public NiveauService(
            NiveauRepository niveauRepo,
            EvaluationHebdoService evalService
    ) {
        this.niveauRepo = niveauRepo;
        this.evalService = evalService;
    }

    /**
     * Création d'un niveau (lié à une rentrée ou indépendant)
     * - évaluations hebdo automatiques
     * - PAS d'endprufung automatique
     */
    public NiveauResponse creer(Niveau niveau) {

        // Barème automatique
        if (niveau.getBareme() == null) {
            niveau.setBareme(
                niveau.getCode().startsWith("A") ? 20 : 100
            );
        }

        Niveau saved = niveauRepo.save(niveau);

        // Création automatique des évaluations hebdo (UNE SEULE FOIS)
        if (!evalService.existePourNiveau(saved.getId())) {
            evalService.creerEvaluationsPourNiveau(saved.getId());
        }

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<NiveauResponse> lister() {
        return niveauRepo.findAll()
                    .stream()
                    .map(this::toResponse)
                    .toList();
    }

    @Transactional(readOnly = true)
    public List<NiveauResponse> listerNiveauxIndependants() {
        return niveauRepo.findAll()
                .stream()
                .filter(n -> n.getRentree() == null)
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public NiveauResponse getById(Long id) {
         Niveau niveau = niveauRepo.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Niveau introuvable")
                );

        return toResponse(niveau);
    }

    //DTO(MAPPING)
    private NiveauResponse toResponse(Niveau n) {

        List<EvaluationHebdoResponse> evals =
                n.getEvaluationsHebdo() == null
                        ? List.of()
                        : n.getEvaluationsHebdo()
                              .stream()
                              .map(e -> new EvaluationHebdoResponse(
                                       e.getId(),
                                       e.getSemaineNum(),
                                       List.of() // les notes sont chargées ailleurs
                                    ))

                              .toList();

        return new NiveauResponse(
                n.getId(),
                n.getCode(),
                n.getBareme(),
                evals
        );
    }
}


