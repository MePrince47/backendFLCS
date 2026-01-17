package FLCS.GESTION.SERVICE;

import FLCS.GESTION.ENTITEES.Niveau;
import FLCS.GESTION.ENTITEES.Rentree;
import FLCS.GESTION.DTO.RentreeResponse;
import FLCS.GESTION.DTO.NiveauResponse;

import FLCS.GESTION.REPOSITORY.RentreeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RentreeService {

    private final RentreeRepository rentreeRepo;
    private final NiveauService niveauService;

    public RentreeService(
            RentreeRepository rentreeRepo,
            NiveauService niveauService
    ) {
        this.rentreeRepo = rentreeRepo;
        this.niveauService = niveauService;
    }

    /**
     * Création d'une rentrée avec niveaux automatiques A1 → B2
     */
    public Rentree creer(Rentree rentree) {

        Rentree saved = rentreeRepo.save(rentree);

        List<String> codes = List.of("A1", "A2", "B1", "B2");

        for (String code : codes) {
            Niveau niveau = Niveau.builder()
                   .code(code)
                   .build();

            niveauService.creer(niveau);
        }

        return saved;
    }

    @Transactional(readOnly = true)
    public List<RentreeResponse> lister() {
        return rentreeRepo.findAll()
            .stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public RentreeResponse getById(Long id) {
        return toResponse(
            rentreeRepo.findById(id)
                .orElseThrow(() ->
                    new IllegalArgumentException("Rentrée introuvable"))
        );
    }
    
    //DTO
    private RentreeResponse toResponse(Rentree r) {
        return new RentreeResponse(
            r.getId(),
            r.getNomRentree(),
            r.getDateDebut(),
            r.getNiveaux()
                .stream()
                .map(n -> new NiveauResponse(
                    n.getId(),
                    n.getCode(),
                    n.getBareme(),
                    List.of() // évaluations non chargées ici
                ))
                .toList()
        );
    }


    // 🔴 CETTE MÉTHODE MANQUAIT
    public List<Rentree> getAllRentrees() {
        return rentreeRepo.findAll();
    }
}

