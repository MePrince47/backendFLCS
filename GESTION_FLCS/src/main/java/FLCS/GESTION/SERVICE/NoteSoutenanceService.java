package FLCS.GESTION.SERVICE;

import FLCS.GESTION.DTO.NoteSoutenanceRequest;
import FLCS.GESTION.DTO.NoteSoutenanceResponse;

import FLCS.GESTION.ENTITEES.Eleve;
import FLCS.GESTION.ENTITEES.Niveau;
import FLCS.GESTION.ENTITEES.NoteSoutenance;

import FLCS.GESTION.REPOSITORY.EleveRepository;
import FLCS.GESTION.REPOSITORY.NoteSoutenanceRepository;
import FLCS.GESTION.REPOSITORY.NiveauRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NoteSoutenanceService {

    private final NoteSoutenanceRepository soutenanceRepo;
    private final EleveRepository eleveRepo;
    private final NiveauRepository niveauRepo;

    public NoteSoutenanceService(
        NoteSoutenanceRepository soutenanceRepo,
        EleveRepository eleveRepo,
        NiveauRepository niveauRepo
    ) {
        this.soutenanceRepo = soutenanceRepo;
        this.eleveRepo = eleveRepo;
        this.niveauRepo = niveauRepo;
    }

    // Attribuer une note de soutenance à un élève

    public NoteSoutenanceResponse attribuer(NoteSoutenanceRequest request) {

        // Vérifier unicité élève + niveau
        soutenanceRepo.findByEleve_IdAndNiveau_Id(
            request.getEleveId(),
            request.getNiveauId()
        ).ifPresent(n -> {
            throw new IllegalStateException(
                "Une note de soutenance existe déjà pour cet élève et ce niveau"
            );
        });

        Eleve eleve = eleveRepo.findById(request.getEleveId())
            .orElseThrow(() -> new IllegalArgumentException("Élève introuvable"));

        Niveau niveau = niveauRepo.findById(request.getNiveauId())
            .orElseThrow(() -> new IllegalArgumentException("Niveau introuvable"));

        // Création de la note
        NoteSoutenance soutenance = NoteSoutenance.builder()
            .eleve(eleve)
            .niveau(niveau)
            .note(request.getNote())
            .build();

        // Sauvegarde
        NoteSoutenance saved = soutenanceRepo.save(soutenance);

        return toResponse(saved);
    }

    // Consulter la note de soutenance d’un élève pour un niveau donné
    
    @Transactional(readOnly = true)
    public NoteSoutenanceResponse consulter(Long eleveId, Long niveauId) {

        NoteSoutenance soutenance =
            soutenanceRepo.findByEleve_IdAndNiveau_Id(eleveId, niveauId)
                .orElseThrow(() ->
                    new IllegalArgumentException("Note de soutenance introuvable")
                );

        return toResponse(soutenance);
    }

    // ========================
    // Mapping interne → DTO
    // ========================
    private NoteSoutenanceResponse toResponse(NoteSoutenance n) {
        return new NoteSoutenanceResponse(
            n.getId(),
            n.getEleve().getNom() + " " + n.getEleve().getPrenom(),
            n.getNiveau().getCode(),
            n.getNote()
        );
    }
}
