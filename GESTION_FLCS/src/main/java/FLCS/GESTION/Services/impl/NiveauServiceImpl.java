package FLCS.GESTION.Services.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.management.relation.RelationNotFoundException;

import org.springframework.stereotype.Service;

import FLCS.GESTION.Dtos.Request.NiveauRequest;
import FLCS.GESTION.Dtos.response.NiveauResponse;
import FLCS.GESTION.Models.Eleve;
import FLCS.GESTION.Models.Enseignant;
import FLCS.GESTION.Models.Niveau;
import FLCS.GESTION.Models.Rentree;
import FLCS.GESTION.Repository.EleveRepository;
import FLCS.GESTION.Repository.EnseignantRepository;
import FLCS.GESTION.Repository.NiveauRepository;
import FLCS.GESTION.Repository.RentreeRepository;
import FLCS.GESTION.Services.NiveauService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NiveauServiceImpl implements NiveauService {

    private static final String UNUSED = null;
    private final NiveauRepository niveauRepository;
    private final RentreeRepository rentreeRepository;
    private final EnseignantRepository enseignantRepository;
    private final EleveRepository eleveRepository;

    @Transactional
    public NiveauResponse createNiveau(NiveauRequest request) {
        log.info("Création d'un nouveau niveau: {}", request.getNom());

        // Vérifier la rentrée
        Rentree rentree = rentreeRepository.findById(request.getRentreeId())
                .orElseThrow();

        // Vérifier si le niveau existe déjà dans cette rentrée
        if (niveauRepository.findByRentreeIdAndNom(rentree.getId(), request.getNom()).isPresent()) {
            throw new IllegalArgumentException("Ce niveau existe déjà dans cette rentrée");
        }

        // Créer le niveau
        Niveau niveau = new Niveau();
        // .nom(request.getNom())
        // .type(Niveau.TypeNiveau.valueOf(request.getType().toUpperCase()))
        // .rentree(rentree)
        // .enseignant(enseignant)
        // .salle(request.getSalle())
        // .capaciteMax(request.getCapaciteMax() != null ? request.getCapaciteMax() :
        // 25)
        // .fraisInscription(request.getFraisInscription() != null ?
        // request.getFraisInscription() : 0.0)
        // .fraisFormation(request.getFraisFormation() != null ?
        // request.getFraisFormation() : 0.0)
        // .statut(Niveau.Statut.PLANIFIE)
        // .build();

        Niveau savedNiveau = niveauRepository.save(niveau);
        log.info("Niveau créé avec succès: ID={}, Nom={}", savedNiveau.getId(), savedNiveau.getNom());

        return mapToResponse(savedNiveau);
    }

    @Override
    @Transactional
    public NiveauResponse updateNiveau(Long id, NiveauRequest request) {
        Niveau niveau = null;
        try {
            niveau = niveauRepository.findById(id)
                    .orElseThrow(() -> new RelationNotFoundException("Niveau non trouvé avec ID: " + id));
        } catch (RelationNotFoundException e) {
            e.printStackTrace();
        }

        // Vérifier si le nom est modifié et s'il existe déjà dans cette rentrée
        if (!niveau.getNom().equals(request.getNom())) {
            niveauRepository.findByRentreeIdAndNom(niveau.getRentree().getId(), request.getNom())
                    .ifPresent(existing -> {
                        if (!existing.getId().equals(id)) {
                            throw new IllegalArgumentException("Ce nom de niveau existe déjà dans cette rentrée");
                        }
                    });
        }

        // Vérifier la rentrée si modifiée
        if (!niveau.getRentree().getId().equals(request.getRentreeId())) {
            Rentree nouvelleRentree = null;
            try {
                nouvelleRentree = rentreeRepository.findById(request.getRentreeId())
                        .orElseThrow(() -> new RelationNotFoundException("Rentrée non trouvée"));
            } catch (RelationNotFoundException e) {
                e.printStackTrace();
            }
            niveau.setRentree(nouvelleRentree);
        }

        // Mettre à jour les autres champs
        niveau.setNom(request.getNom());
        niveau.setSalle(request.getSalle());
        niveau.setCapaciteMax(request.getCapaciteMax());

        Niveau updatedNiveau = niveauRepository.save(niveau);
        log.info("Niveau mis à jour: ID={}, Nom={}", updatedNiveau.getId(), updatedNiveau.getNom());

        return mapToResponse(updatedNiveau);
    }

    @Override
    @Transactional
    public void deleteNiveau(Long id) {
        Niveau niveau = null;
        try {
            niveau = niveauRepository.findById(id)
                    .orElseThrow(() -> new RelationNotFoundException("Niveau non trouvé avec ID: " + id));
        } catch (RelationNotFoundException e) {

            e.printStackTrace();
        }

        // Vérifier si le niveau a des élèves
        List<Eleve> nombreEleves = eleveRepository.findByNiveauId(id);
        niveauRepository.delete(niveau);
        log.info("Niveau supprimé: ID={}, Nom={}", niveau.getId(), niveau.getNom());
    }

    @Override
    public NiveauResponse getNiveauById(Long id) {
        Niveau niveau = null;
        try {
            niveau = niveauRepository.findById(id)
                    .orElseThrow(() -> new RelationNotFoundException("Niveau non trouvé avec ID: " + id));
        } catch (RelationNotFoundException e) {
            e.printStackTrace();
        }
        return mapToResponse(niveau);
    }

    @Override
    public List<NiveauResponse> getAllNiveaux() {
        return niveauRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<NiveauResponse> getNiveauxByRentree(Long rentreeId) {
        // Vérifier que la rentrée existe
        try {
            rentreeRepository.findById(rentreeId)
                    .orElseThrow(() -> new RelationNotFoundException("Rentrée non trouvée"));
        } catch (RelationNotFoundException e) {

            e.printStackTrace();
        }

        return niveauRepository.findByRentreeId(rentreeId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public NiveauResponse assignerEnseignant(Long niveauId, Long enseignantId) {
        Niveau niveau = null;
        try {
            niveau = niveauRepository.findById(niveauId)
                    .orElseThrow(() -> new RelationNotFoundException("Niveau non trouvé"));
        } catch (RelationNotFoundException e) {

            e.printStackTrace();
        }

        // niveau.setEnseignant(enseignant);
        Niveau updatedNiveau = niveauRepository.save(niveau);

        log.info("Enseignant {} assigné au niveau {}", enseignantId, niveauId);
        return mapToResponse(updatedNiveau);
    }

    @Override
    @Transactional
    public NiveauResponse retirerEnseignant(Long niveauId) {
        Niveau niveau = null;
        try {
            niveau = niveauRepository.findById(niveauId)
                    .orElseThrow(() -> new RelationNotFoundException("Niveau non trouvé"));
        } catch (RelationNotFoundException e) {
            e.printStackTrace();
        }

        niveau.setEnseignant(null);
        Niveau updatedNiveau = niveauRepository.save(niveau);

        log.info("Enseignant retiré du niveau {}", niveauId);
        return mapToResponse(updatedNiveau);
    }

    private NiveauResponse mapToResponse(Niveau niveau) {
        Long nombreEleves = niveauRepository.countElevesByNiveauId(niveau.getId());
        Long nombreNiveaux = niveau.getRentree() != null
                ? niveauRepository.countByRentreeId(niveau.getRentree().getId())
                : 0L;

        return NiveauResponse.fromEntity(niveau, nombreNiveaux, nombreEleves);

    }

    @Override
    public Long countElevesByNiveau(Long niveauId) {
        return niveauRepository.countElevesByNiveauId(niveauId);
    }

    @Override
    public boolean estComplet(Long niveauId) {
        Niveau niveau = null;
        try {
            niveau = niveauRepository.findById(niveauId)
                    .orElseThrow(() -> new RelationNotFoundException("Niveau non trouvé avec ID: " + niveauId));
        } catch (RelationNotFoundException e) {
        
            e.printStackTrace();
        }
        return niveau.estComplet();
    }

    @Override
    public List<NiveauResponse> getNiveauxByEnseignant(Long enseignantId) {
        return niveauRepository.findByEnseignantId(enseignantId).stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public NiveauResponse updateNiveau(Long id, NiveauResponse niveauResponse) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateNiveau'");
    }

    @Override
    public NiveauResponse createNiveau(NiveauResponse niveauResponse) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createNiveau'");
    }

}
