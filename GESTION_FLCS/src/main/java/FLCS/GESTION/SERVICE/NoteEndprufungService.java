package FLCS.GESTION.SERVICE;

import FLCS.GESTION.ENTITEES.NoteEndprufung;
import FLCS.GESTION.ENTITEES.Endprufung;
import FLCS.GESTION.ENTITEES.Eleve;
import FLCS.GESTION.ENTITEES.Niveau;
import FLCS.GESTION.ENTITEES.TypeNote;

import FLCS.GESTION.EXCEPTION.NoteValidationException;

import FLCS.GESTION.REPOSITORY.NoteEndprufungRepository;
import FLCS.GESTION.REPOSITORY.EndprufungRepository;
import FLCS.GESTION.REPOSITORY.EleveRepository;

import FLCS.GESTION.DTO.NoteEndprufungRequest;
import FLCS.GESTION.DTO.NoteResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NoteEndprufungService {

    private final NoteEndprufungRepository noteRepo;
    private final EndprufungRepository endRepo;
    private final EleveRepository eleveRepo;

    public NoteEndprufungService(
            NoteEndprufungRepository noteRepo,
            EndprufungRepository endRepo,
            EleveRepository eleveRepo
    ) {
        this.noteRepo = noteRepo;
        this.endRepo = endRepo;
        this.eleveRepo = eleveRepo;
    }

    // CREATE
    public NoteResponse creer(NoteEndprufungRequest req) {

        Endprufung end = endRepo.findById(req.endprufungId())
                .orElseThrow(() -> new IllegalArgumentException("Endprufung introuvable"));

        Niveau niveau = end.getNiveau();

        if (niveau.isCloture()) {
        throw new IllegalStateException(
                "Niveau clôturé, saisie des notes interdite"
        );
        }

        Eleve eleve = eleveRepo.findById(req.eleveId())
                .orElseThrow(() -> new IllegalArgumentException("Élève introuvable"));

        // vérification appartenance élève au niveau
        if (!eleve.getNiveauLangue().getId()
                .equals(end.getNiveau().getId())) {

        throw new IllegalStateException(
                "Cet élève n'appartient pas à ce niveau"
        );
        }

        // unicité métier
        noteRepo.findByEndprufung_IdAndEleve_Id(
                req.endprufungId(), req.eleveId()
        ).ifPresent(n -> {
            throw new IllegalStateException("Note Endprufung déjà saisie");
        });

        validerNotes(end.getNiveau(),
                req.les(), req.hor(), req.schreib(), req.gramm(), req.spre()
        );

        NoteEndprufung note = noteRepo.save(
                NoteEndprufung.builder()
                        .endprufung(end)
                        .eleve(eleve)
                        .les(req.les())
                        .hor(req.hor())
                        .schreib(req.schreib())
                        .gramm(req.gramm())
                        .spre(req.spre())
                        .build()
        );

        return toResponse(note);
    }

    // UPDATE
    public NoteResponse modifierParEleveEtNiveau(
        Long eleveId,
        Long niveauId,
        NoteEndprufungRequest req
        ) {
        NoteEndprufung note = noteRepo
                .findByEleve_IdAndEndprufung_Niveau_Id(eleveId, niveauId)
                .orElseThrow(() ->
                new IllegalArgumentException(
                        "Note d’Endprüfung introuvable pour cet élève"
                )
                );

        Niveau niveau = note.getEndprufung().getNiveau();

        if (niveau.isCloture()) {
                throw new IllegalStateException(
                "Niveau clôturé, modification interdite"
                );
        }

        validerNotes(
                niveau,
                req.les(),
                req.hor(),
                req.schreib(),
                req.gramm(),
                req.spre()
        );

        note.setLes(req.les());
        note.setHor(req.hor());
        note.setSchreib(req.schreib());
        note.setGramm(req.gramm());
        note.setSpre(req.spre());

        return toResponse(note);
    }


    // READ : résultats finaux d’un niveau
    @Transactional(readOnly = true)
    public List<NoteResponse> lireParNiveau(Long niveauId) {
        return noteRepo.findByEndprufung_Niveau_Id(niveauId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // READ : résultat final d’un élève pour un niveau
    @Transactional(readOnly = true)
    public NoteResponse lireParEleveEtNiveau(Long eleveId, Long niveauId) {
        return noteRepo.findByEleve_IdAndEndprufung_Niveau_Id(eleveId, niveauId)
                .map(this::toResponse)
                .orElseThrow(() ->
                        new IllegalArgumentException("Résultat Endprufung introuvable")
                );
    }

    // VALIDATION
    private void validerNotes(Niveau niveau, Double... notes) {
        double max = niveau.getBareme();
        for (Double note : notes) {
            if (note < 0 || note > max) {
                throw new NoteValidationException(
                        "Note invalide (0 à " + max + ") pour le niveau " + niveau.getCode()
                );
            }
        }
    }

    // DTO
    private NoteResponse toResponse(NoteEndprufung n) {
        return new NoteResponse(
        n.getEleve().getId(),
        n.getEleve().getNom() + " " + n.getEleve().getPrenom(),
        n.getEndprufung().getNiveau().getId(),
        n.getEndprufung().getNiveau().getCode(),
        null,                     // pas de semaine
        TypeNote.ENDPRUFUNG,
        n.getLes(),
        n.getHor(),
        n.getSchreib(),
        n.getGramm(),
        n.getSpre()
        );
    }
}
