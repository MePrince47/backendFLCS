package FLCS.GESTION.SERVICE;

import FLCS.GESTION.ENTITEES.Paiement;
import FLCS.GESTION.ENTITEES.Eleve;
import FLCS.GESTION.REPOSITORY.PaiementRepository;
import FLCS.GESTION.REPOSITORY.EleveRepository;

import FLCS.GESTION.EXCEPTION.EleveNotFoundException;

import FLCS.GESTION.DTO.SearchResponse;
import FLCS.GESTION.DTO.PaiementResponse;
import FLCS.GESTION.DTO.PaiementResumeResponse;
import FLCS.GESTION.DTO.PaiementHistoriqueResponse;
import FLCS.GESTION.DTO.PaiementRequest;
import FLCS.GESTION.DTO.PaiementExport;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PaiementService {

    private final PaiementRepository paiementRepository;
    private final EleveRepository eleveRepository;

    public PaiementService(
            PaiementRepository paiementRepository,
            EleveRepository eleveRepository) {
        this.paiementRepository = paiementRepository;
        this.eleveRepository = eleveRepository;
    }

    // CREATE
    @Transactional
    public PaiementResponse create(PaiementRequest request) {

        //  Référence de virement unique
        if (paiementRepository.existsByReferenceVirement(request.referenceVirement())) {
            throw new IllegalArgumentException("Référence de virement déjà utilisée");
        }

        // Élève existant
        Long eleveId = request.eleveId();

        Eleve eleve = eleveRepository.findById(eleveId)
            .orElseThrow(() -> new EleveNotFoundException(eleveId));

        //  Calcul financier
        Double totalPaye = paiementRepository.totalPayeParEleve(eleve.getId());
        if (totalPaye == null) totalPaye = 0.0;

        Double montantTotal = eleve.getMontantTotal();
        Double resteAPayer = montantTotal - totalPaye;

        //  BLOQUAGE si solde déjà réglé
        if (resteAPayer <= 0) {
            throw new IllegalStateException(
                "Paiement refusé : le solde est déjà intégralement réglé"
            );
        }

        //  BLOQUAGE si paiement trop élevé
        if (request.montant() > resteAPayer) {
            throw new IllegalArgumentException(
                "Montant supérieur au reste à payer (" + resteAPayer + ")"
            );
        }

        //  Création du paiement
        Paiement paiement = Paiement.builder()
                .montant(request.montant())
                .datePaiement(request.datePaiement())
                .referenceVirement(request.referenceVirement())
                .eleve(eleve)
                .build();

        Paiement saved = paiementRepository.save(paiement);

        return mapToResponse(saved);
    }

    // READ
    public List<PaiementResponse> getByEleve(Long eleveId) {
        return paiementRepository.findByEleve_Id(eleveId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private PaiementResponse mapToResponse(Paiement p) {
        return new PaiementResponse(
                p.getId(),
                p.getMontant(),
                p.getDatePaiement(),
                p.getReferenceVirement(),
                p.getEleve().getId(),
                p.getEleve().getNom() + " " + p.getEleve().getPrenom()
        );
    }

    // CALCUL DU RESTE A PAYER
    @Transactional(readOnly = true)
    public PaiementResumeResponse getResumePaiement(Long eleveId) {

        Eleve eleve = eleveRepository.findById(eleveId)
            .orElseThrow(() -> new EleveNotFoundException(eleveId));

        Double totalPaye = paiementRepository.totalPayeParEleve(eleveId);
        Double reste = eleve.getMontantTotal() - totalPaye;

        return new PaiementResumeResponse(
            eleve.getId(),
            eleve.getNom() + " " + eleve.getPrenom(),
            eleve.getMontantTotal(),
            totalPaye,
            reste
        );
    }

    // HISTORIQUE DE PAIEMENT PAR ELEVE
    @Transactional(readOnly = true)
    public List<PaiementHistoriqueResponse> historique(Long eleveId) {

        return paiementRepository.findByEleveIdOrderByDatePaiementDesc(eleveId)
            .stream()
            .map(p -> new PaiementHistoriqueResponse(
                p.getId(),
                p.getMontant(),
                p.getDatePaiement(),
                p.getReferenceVirement()
            ))
            .toList();
    }

    // EXPORT DES INFOS RELATIFS AUX PAIEMENTS D'UN ELEVE
    @Transactional(readOnly = true)
    public PaiementExport exportPaiements(Long eleveId) {

        Eleve eleve = eleveRepository.findById(eleveId)
            .orElseThrow(() -> new EleveNotFoundException(eleveId));

        Double totalPaye = paiementRepository.totalPayeParEleve(eleveId);
        Double reste = eleve.getMontantTotal() - totalPaye;

        List<PaiementHistoriqueResponse> historique = historique(eleveId);

        return new PaiementExport(
            eleve.getNom() + " " + eleve.getPrenom(),
            eleve.getMontantTotal(),
            totalPaye,
            reste,
            historique
        );
    }



}
