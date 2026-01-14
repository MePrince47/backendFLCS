package FLCS.GESTION.SERVICE;

import FLCS.GESTION.ENTITEES.Eleve;
import FLCS.GESTION.ENTITEES.Partenaire;
import FLCS.GESTION.ENTITEES.Niveau;

import FLCS.GESTION.REPOSITORY.EleveRepository;
import FLCS.GESTION.REPOSITORY.PartenaireRepository;
import FLCS.GESTION.REPOSITORY.NiveauRepository;

import FLCS.GESTION.EXCEPTION.EleveNotFoundException;
import FLCS.GESTION.DTO.SearchResponse;
import FLCS.GESTION.DTO.EleveResponse;
import FLCS.GESTION.DTO.EleveRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class EleveService {

    private final EleveRepository eleveRepository;

    private final PartenaireRepository partenaireRepository;
    private final NiveauRepository niveauRepository;

    public EleveService(
        EleveRepository eleveRepository,
        PartenaireRepository partenaireRepository,
        NiveauRepository niveauRepository
    ) {
        this.eleveRepository = eleveRepository;
        this.partenaireRepository = partenaireRepository;
        this.niveauRepository = niveauRepository;
    }


    //AFFICHAGE D'UN ELEVE
    private EleveResponse mapToResponse(Eleve e) {
        return new EleveResponse(
            e.getId(),
            e.getNom(),
            e.getPrenom(),
            e.getDateNaiss(),
            e.getNiveauScolaire(),
            e.getTypeProcedure(),    
            e.getMontantTotal(),      
            e.getTelCandidat(),
            e.getTelParent(),
            e.getStatut(),            
            e.getPartenaire() != null ? e.getPartenaire().getNomPartenaire() : null,
            e.getNiveauLangue() != null ? e.getNiveauLangue().getCode() : null,
            e.getRentree() != null ? e.getRentree().getNomRentree() : null
        );
    }

    // CREATE
    @Transactional
    public EleveResponse create(EleveRequest request) {

        Partenaire partenaire = partenaireRepository
            .findByNomPartenaire(request.nomPartenaire())
            .orElseThrow(() -> new IllegalArgumentException("Partenaire introuvable"));

        Niveau niveau;

        if (request.nomRentree() == null) {
            // niveau indépendant
            niveau = niveauRepository
                .findByCodeAndRentreeIsNull(request.codeNiveau())
                .orElseThrow(() ->
                    new IllegalArgumentException(
                        "Niveau indépendant " + request.codeNiveau() + " introuvable"
                    )
                );
        } else {
            niveau = niveauRepository
                .findByCodeAndRentree_NomRentree(
                    request.codeNiveau(),
                    request.nomRentree()
                )
                .orElseThrow(() ->
                    new IllegalArgumentException(
                        "Niveau " + request.codeNiveau() +
                        " introuvable pour la rentrée " + request.nomRentree()
                    )
                );
        }

        Eleve eleve = Eleve.builder()
            .nom(request.nom())
            .prenom(request.prenom())
            .dateNaiss(request.dateNaiss())
            .niveauScolaire(request.niveauScolaire())
            .typeProcedure(request.typeProcedure())
            .montantTotal(request.montantTotal()) 
            .telCandidat(request.telCandidat())
            .telParent(request.telParent())
            .statut(request.statut())
            .partenaire(partenaire)
            .niveauLangue(niveau)
            .rentree(niveau.getRentree()) // peut être null
            .build();

        Eleve saved = eleveRepository.save(eleve);

        return mapToResponse(saved);
    }



    // READ ALL
    public List<EleveResponse> getAll() {
        return eleveRepository.findAll()
            .stream()
            .map(this::mapToResponse)
            .toList();
    }


    // READ BY ID
    public EleveResponse getById(Long id) {

        Eleve eleve = eleveRepository.findById(id)
            .orElseThrow(() -> new EleveNotFoundException(id));

        return mapToResponse(eleve);
    }


    // UPDATE
    @Transactional
    public EleveResponse update(Long id, EleveRequest request) {

        Eleve existant = eleveRepository.findById(id)
            .orElseThrow(() -> new EleveNotFoundException(id));

        existant.setNom(request.nom());
        existant.setPrenom(request.prenom());
        existant.setDateNaiss(request.dateNaiss());
        existant.setNiveauScolaire(request.niveauScolaire());
        existant.setTypeProcedure(request.typeProcedure());
        existant.setTelCandidat(request.telCandidat());
        existant.setTelParent(request.telParent());
        existant.setStatut(request.statut());

        Partenaire partenaire = partenaireRepository
            .findByNomPartenaire(request.nomPartenaire())
            .orElseThrow(() -> new IllegalArgumentException("Partenaire introuvable"));

        Niveau niveau = niveauRepository
            .findByCodeAndRentree_NomRentree(
                request.codeNiveau(),
                request.nomRentree()
            )
            .orElseThrow(() ->
                new IllegalArgumentException(
                    "Niveau " + request.codeNiveau() +
                    " introuvable pour la rentrée " + request.nomRentree()
                )
            );
        existant.setPartenaire(partenaire);
        existant.setNiveauLangue(niveau);
        existant.setRentree(niveau.getRentree());

        return mapToResponse(existant);
    }


    // DELETE
    public void delete(Long id) {
        eleveRepository.deleteById(id);
    }

    // SEARCH BY NAME
    public List<EleveResponse> searchByNom(String keyword) {
        return eleveRepository
            .findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(keyword, keyword)
            .stream()
            .map(this::mapToResponse)
            .toList();
    }

    // FILTER BY NIVEAU_LANGUE
    public List<EleveResponse> findByNiveauLangue_Code(String code) {
        return eleveRepository
            .findByNiveauLangue_Code(code)
            .stream()
            .map(this::mapToResponse)
            .toList();
    }

    // RECHERCHE AVANCEE
    public SearchResponse<EleveResponse> rechercheAvancee(
        String nom,
        String niveauScolaire,
        String rentree,
        String niveauLangue,
        String partenaire
    ) {

        List<EleveResponse> resultats = eleveRepository.rechercheAvancee(
                nom, niveauScolaire, rentree, niveauLangue, partenaire
            )
            .stream()
            .map(this::mapToResponse)
            .toList();

        if (resultats.isEmpty()) {
            return new SearchResponse<>(
                0,
                "Aucun élément ne correspond à votre recherche",
                resultats
            );
        }

        return new SearchResponse<>(
            resultats.size(),
            resultats.size() + " élément(s) trouvé(s)",
            resultats
        );
    }

    // LISTE DES ELEVES D'UN NIVEAU
    @Transactional(readOnly = true)
    public List<EleveResponse> getByNiveau(Long niveauId) {
        return eleveRepository.findByNiveauLangue_Id(niveauId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // LISTE DES ELEVES D'UNE RENTREE
    @Transactional(readOnly = true)
    public List<EleveResponse> getByRentree(Long rentreeId) {
        return eleveRepository.findByNiveauLangue_Rentree_Id(rentreeId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    
    }

    