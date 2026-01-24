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
     * Création d'une rentrée avec génération automatique
     * des niveaux A1 → B2 liés à cette rentrée.
     *
     * Exemple :
     *  - Rentrée : "Septembre 2025"
     *  - Codes générés :
     *      A1_SEPTEMBRE_2025
     *      A2_SEPTEMBRE_2025
     *      B1_SEPTEMBRE_2025
     *      B2_SEPTEMBRE_2025
     */
    public Rentree creer(Rentree rentree) {

        Rentree saved = rentreeRepo.save(rentree);

        List<String> niveauxBase = List.of("A1", "A2", "B1", "B2");

        String suffixeRentree = normaliser(saved.getNomRentree());

        for (String base : niveauxBase) {

            String codeNiveau = base + "_" + suffixeRentree;

            Niveau niveau = Niveau.builder()
                .code(codeNiveau)
                .rentree(saved)
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

    // ================== DTO ==================

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

    // ================== UTILITAIRE ==================

    /**
     * Normalise un nom de rentrée pour l'utiliser dans un code technique.
     * Exemples :
     *  - "Septembre 2025" → SEPTEMBRE_2025
     *  - "Janvier-2024"   → JANVIER_2024
     */
    private String normaliser(String valeur) {
        return valeur
            .toUpperCase()
            .replaceAll("[^A-Z0-9]", "_")
            .replaceAll("_+", "_");
    }
}
