package FLCS.GESTION.SERVICE;

import FLCS.GESTION.ENTITEES.NoteHebdo;
import FLCS.GESTION.ENTITEES.EvaluationHebdo;
import FLCS.GESTION.ENTITEES.Eleve;
import FLCS.GESTION.ENTITEES.Niveau;
import FLCS.GESTION.ENTITEES.TypeNote;

import FLCS.GESTION.EXCEPTION.NoteValidationException;

import FLCS.GESTION.REPOSITORY.NoteHebdoRepository;
import FLCS.GESTION.REPOSITORY.EvaluationHebdoRepository;
import FLCS.GESTION.REPOSITORY.EleveRepository;

import FLCS.GESTION.DTO.NoteHebdoRequest;
import FLCS.GESTION.DTO.NoteResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NoteHebdoService {

    private final NoteHebdoRepository noteRepo;
    private final EvaluationHebdoRepository evalRepo;
    private final EleveRepository eleveRepo;

    public NoteHebdoService(
            NoteHebdoRepository noteRepo,
            EvaluationHebdoRepository evalRepo,
            EleveRepository eleveRepo
    ) {
        this.noteRepo = noteRepo;
        this.evalRepo = evalRepo;
        this.eleveRepo = eleveRepo;
    }

    // CREATE
    public NoteResponse saisir(NoteHebdoRequest req) {

        EvaluationHebdo eval = evalRepo.findById(req.evaluationHebdoId())
                .orElseThrow(() -> new IllegalArgumentException("Évaluation introuvable"));

        Niveau niveau = eval.getNiveau();

        if (niveau.isCloture()) {
        throw new IllegalStateException(
                "Niveau clôturé, saisie des notes interdite"
        );
        }

        Eleve eleve = eleveRepo.findById(req.eleveId())
                .orElseThrow(() -> new IllegalArgumentException("Élève introuvable"));

        // vérification appartenance élève au niveau
        if (!eleve.getNiveauLangue().getId()
                .equals(eval.getNiveau().getId())) {

            throw new IllegalStateException(
                "Cet élève n'appartient pas à ce niveau"
             );
        }

        // unicité métier
        noteRepo.findByEvaluationHebdo_IdAndEleve_Id(
                req.evaluationHebdoId(), req.eleveId()
        ).ifPresent(n -> {
            throw new IllegalStateException("Note hebdomadaire déjà saisie");
        });

        validerNotes(eval.getNiveau(),
                req.les(), req.hor(), req.schreib(), req.gramm(), req.spre()
        );

        NoteHebdo note = noteRepo.save(
                NoteHebdo.builder()
                        .evaluationHebdo(eval)
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
        public NoteResponse modifierParEleveEtEvaluation(
                Long eleveId,
                Long evaluationHebdoId,
                NoteHebdoRequest req
        ) {
        NoteHebdo note = noteRepo
                .findByEvaluationHebdo_IdAndEleve_Id(evaluationHebdoId, eleveId)
                .orElseThrow(() ->
                new IllegalArgumentException(
                        "Note hebdomadaire introuvable pour cet élève"
                )
                );

        Niveau niveau = note.getEvaluationHebdo().getNiveau();

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


    // READ : toutes les notes d’un niveau
    @Transactional(readOnly = true)
    public List<NoteResponse> lireParNiveau(Long niveauId) {
        return noteRepo.findByEvaluationHebdo_Niveau_Id(niveauId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // READ : notes d’un niveau pour une semaine
    @Transactional(readOnly = true)
    public List<NoteResponse> lireParNiveauEtSemaine(
            Long niveauId,
            Integer semaine
    ) {
        return noteRepo
                .findByEvaluationHebdo_Niveau_IdAndEvaluationHebdo_SemaineNum(
                        niveauId, semaine
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // READ : notes d’un élève pour un niveau
    @Transactional(readOnly = true)
    public List<NoteResponse> lireParEleveEtNiveau(
            Long eleveId,
            Long niveauId
    ) {
        return noteRepo
                .findByEleve_IdAndEvaluationHebdo_Niveau_Id(eleveId, niveauId)
                .stream()
                .map(this::toResponse)
                .toList();
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
    private NoteResponse toResponse(NoteHebdo n) {
        return new NoteResponse(
        n.getEleve().getId(),
        n.getEleve().getNom() + " " + n.getEleve().getPrenom(),
        n.getEvaluationHebdo().getNiveau().getId(),
        n.getEvaluationHebdo().getNiveau().getCode(),
        n.getEvaluationHebdo().getSemaineNum(),
        TypeNote.HEBDO,
        n.getLes(),
        n.getHor(),
        n.getSchreib(),
        n.getGramm(),
        n.getSpre()
        );
    }
}
