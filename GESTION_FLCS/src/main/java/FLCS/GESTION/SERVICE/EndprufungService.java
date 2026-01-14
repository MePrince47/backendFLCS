package FLCS.GESTION.SERVICE;

import FLCS.GESTION.ENTITEES.NoteEndprufung;
import FLCS.GESTION.ENTITEES.Endprufung;
import FLCS.GESTION.ENTITEES.Eleve;
import FLCS.GESTION.ENTITEES.Niveau;

import FLCS.GESTION.REPOSITORY.NoteEndprufungRepository;
import FLCS.GESTION.REPOSITORY.EndprufungRepository;
import FLCS.GESTION.REPOSITORY.EleveRepository;
import FLCS.GESTION.REPOSITORY.NiveauRepository;

import FLCS.GESTION.DTO.NoteEndprufungRequest;
import FLCS.GESTION.DTO.NoteResponse;
import FLCS.GESTION.DTO.NoteResponseDTO;
import FLCS.GESTION.DTO.EndprufungResponse;
import FLCS.GESTION.DTO.EndprufungRequest; 


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.time.LocalDate;

@Service
@Transactional
public class EndprufungService {

    private final EndprufungRepository endRepo;
    private final NiveauRepository niveauRepo;

    public EndprufungService(
            EndprufungRepository endRepo,
            NiveauRepository niveauRepo
    ) {
        this.endRepo = endRepo;
        this.niveauRepo = niveauRepo;
    }

    /**
     * Création manuelle de l’examen final (DTO)
     */
    public EndprufungResponse creer(EndprufungRequest request) {

        Niveau niveau = niveauRepo.findById(request.niveauId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Niveau introuvable")
                );

        if (endRepo.existsByNiveau(niveau)) {
            throw new IllegalStateException(
                    "Endprufung déjà existant pour ce niveau"
            );
        }

        Endprufung endprufung = Endprufung.builder()
                .dateExam(request.dateExam())
                .niveau(niveau)
                .build();

        Endprufung saved = endRepo.save(endprufung);

        return toResponse(saved);
    }

    /**
     * Consultation par niveau
     */
    @Transactional(readOnly = true)
    public EndprufungResponse getByNiveau(Long niveauId) {

        Endprufung end = endRepo.findByNiveau_Id(niveauId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Endprufung introuvable pour ce niveau"
                        )
                );

        return toResponse(end);
    }

    public boolean existePourNiveau(Long niveauId) {

        Niveau niveau = niveauRepo.findById(niveauId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Niveau introuvable")
                );

        return endRepo.existsByNiveau(niveau);
    }

    // ===================== MAPPING =====================

    private EndprufungResponse toResponse(Endprufung e) {

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

        return new EndprufungResponse(
                e.getId(),
                e.getDateExam(),
                notes
        );
    }
}


