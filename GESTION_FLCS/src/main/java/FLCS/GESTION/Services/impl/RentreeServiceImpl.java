package FLCS.GESTION.Services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import FLCS.GESTION.Dtos.Request.RentreeRequest;
import FLCS.GESTION.Dtos.response.RentreeResponse;
import FLCS.GESTION.Models.Niveau;
import FLCS.GESTION.Models.Rentree;
import FLCS.GESTION.Repository.NiveauRepository;
import FLCS.GESTION.Repository.RentreeRepository;
import FLCS.GESTION.Services.RentreeService;

import java.lang.module.ResolutionException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RentreeServiceImpl implements RentreeService {

    private final RentreeRepository rentreeRepository = null;
    private final NiveauRepository niveauRepository = null;

    @Override
    @Transactional
    public RentreeResponse createRentree(RentreeRequest request) {
        log.info("Création d'une nouvelle rentrée: {}", request.getCode());

        // Vérifier si le code existe déjà
        if (rentreeRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("Une rentrée avec ce code existe déjà");
        }

        // Créer la rentrée
        Rentree rentree = new Rentree();
        rentree.setCode(request.getCode());
        rentree.setNom(request.getNom());
        rentree.setDescription(request.getDescription());
        rentree.setDateDebut(request.getDateDebut());
        rentree.setDateFin(request.getDateFin());
        rentree.setNombrePlacesMax(request.getNombrePlacesMax());
        rentree.setNombrePlacesPrises(0);
        rentree.setStatut(Rentree.Statut.PLANIFIEE);

        Rentree savedRentree = rentreeRepository.save(rentree);

        // Créer automatiquement les niveaux A1, A2, B1, B2
        creerNiveauxPourRentree(savedRentree.getId());

        log.info("Rentrée créée avec succès: {}", savedRentree.getCode());
        return toResponse(savedRentree);
    }

    @Override
    @Transactional
    public void creerNiveauxPourRentree(Long rentreeId) {
        Rentree rentree = rentreeRepository.findById(rentreeId)
                .orElseThrow(() -> new ResolutionException("Rentrée non trouvée"));

        List<String> nomsNiveaux = Arrays.asList("A1", "A2", "B1", "B2", "C1");

        for (String nomNiveau : nomsNiveaux) {
            // Vérifier si le niveau existe déjà
            if (niveauRepository.findByRentreeIdAndNom(rentreeId, nomNiveau).isEmpty()) {
                Niveau niveau = new Niveau();
                // niveau.setNom(nomNiveau);
                // niveau.setType(Niveau.TypeNiveau.RENTREE);
                // niveau.setRentree(rentree);
                // niveau.setStatut(Niveau.Statut.PLANIFIE);
                niveauRepository.save(niveau);

                log.info("Niveau {} créé pour la rentrée {}", nomNiveau, rentree.getCode());
            }
        }
    }

    @Override
    @Transactional
    public RentreeResponse updateRentree(Long id, RentreeRequest request) {
        Rentree rentree = rentreeRepository.findById(id)
                .orElseThrow(() -> new ResolutionException("Rentrée non trouvée"));

        // Vérifier si le code est modifié et s'il existe déjà
        if (!rentree.getCode().equals(request.getCode()) &&
                rentreeRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("Une rentrée avec ce code existe déjà");
        }

        rentree.setCode(request.getCode());
        rentree.setNom(request.getNom());
        rentree.setDescription(request.getDescription());
        rentree.setDateDebut(request.getDateDebut());
        rentree.setDateFin(request.getDateFin());
        rentree.setNombrePlacesMax(request.getNombrePlacesMax());

        // Mettre à jour le statut selon les dates
        LocalDate aujourdhui = LocalDate.now();
        if (aujourdhui.isBefore(rentree.getDateDebut())) {
            rentree.setStatut(Rentree.Statut.PLANIFIEE);
        } else if (aujourdhui.isAfter(rentree.getDateFin())) {
            rentree.setStatut(Rentree.Statut.TERMINEE);
        } else {
            rentree.setStatut(Rentree.Statut.EN_COURS);
        }

        Rentree updatedRentree = rentreeRepository.save(rentree);
        return toResponse(updatedRentree);
    }

    @Override
    @Transactional
    public void deleteRentree(Long id) {
        Rentree rentree = rentreeRepository.findById(id)
                .orElseThrow(() -> new ResolutionException("Rentrée non trouvée"));

        // Vérifier si la rentrée a des élèves
        Long nombreEleves = rentreeRepository.countElevesByRentreeId(id);
        if (nombreEleves > 0) {
            throw new IllegalStateException("Impossible de supprimer une rentrée avec des élèves");
        }

        rentreeRepository.delete(rentree);
        log.info("Rentrée supprimée: {}", rentree.getCode());
    }

    @Override
    public RentreeResponse getRentreeById(Long id) {
        Rentree rentree = rentreeRepository.findById(id)
                .orElseThrow(() -> new ResolutionException("Rentrée non trouvée"));
        return toResponse(rentree);
    }

    @Override
    public RentreeResponse getRentreeByCode(String code) {
        Rentree rentree = rentreeRepository.findByCode(code).orElseThrow();
        return toResponse(rentree);
    }

    @Override
    public List<RentreeResponse> getAllRentrees() {
        return rentreeRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RentreeResponse> getRentreesByStatut(String statut) {
        Rentree.Statut statutEnum;
        try {
            statutEnum = Rentree.Statut.valueOf(statut.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Statut invalide: " + statut);
        }

        return rentreeRepository.findByStatut(statutEnum).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private RentreeResponse toResponse(Rentree rentree) {
        Long nombreNiveaux = niveauRepository.countElevesByNiveauId(rentree.getId());
        Long nombreEleves = rentreeRepository.countElevesByRentreeId(rentree.getId());

        return RentreeResponse.fromEntity(rentree, nombreNiveaux, nombreEleves);
    }

    @Override
    public Long countRentrees() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'countRentrees'");
    }

    @Override
    public List<RentreeResponse> getRentreesEnCours() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRentreesEnCours'");
    }
}
