package FLCS.GESTION.SERVICE;

import FLCS.GESTION.DTO.*;
import FLCS.GESTION.ENTITEES.*;
import FLCS.GESTION.REPOSITORY.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
public class BulletinService {

    private final EleveRepository eleveRepo;
    private final NoteHebdoRepository noteHebdoRepo;
    private final NoteEndprufungRepository endRepo;
    private final ResultatRepository resultatRepo;

    public BulletinService(
        EleveRepository eleveRepo,
        NoteHebdoRepository noteHebdoRepo,
        NoteEndprufungRepository endRepo,
        ResultatRepository resultatRepo
    ) {
        this.eleveRepo = eleveRepo;
        this.noteHebdoRepo = noteHebdoRepo;
        this.endRepo = endRepo;
        this.resultatRepo = resultatRepo;
    }

    public BulletinDTO generer(Long eleveId, Long niveauId) {

        Eleve eleve = eleveRepo.findById(eleveId)
            .orElseThrow(() ->
                new IllegalArgumentException("Élève introuvable"));

        String niveauCode = eleve.getNiveauLangue().getCode();
        String codeCourt = niveauCode.split("_")[0];

        // ================== NOTES HEBDO ==================
        List<NoteResponse> hebdos =
            noteHebdoRepo
                .findByEleve_IdAndEvaluationHebdo_Niveau_Id(eleveId, niveauId)
                .stream()
                .map(n -> new NoteResponse(
                    eleve.getId(),
                    eleve.getNom() + " " + eleve.getPrenom(),
                    niveauId,
                    niveauCode,
                    n.getEvaluationHebdo().getSemaineNum(),
                    TypeNote.HEBDO,
                    n.getLes(),
                    n.getHor(),
                    n.getSchreib(),
                    n.getGramm(),
                    n.getSpre()
                ))
                .toList();

        // ================== ENDPRÜFUNG ==================
        List<NoteResponse> endNotes =
            endRepo
                .findByEleve_IdAndEndprufung_Niveau_Id(eleveId, niveauId)
                .map(e -> List.of(
                    new NoteResponse(
                        eleve.getId(),
                        eleve.getNom() + " " + eleve.getPrenom(),
                        niveauId,
                        niveauCode,
                        null,
                        TypeNote.ENDPRUFUNG,
                        e.getLes(),
                        e.getHor(),
                        e.getSchreib(),
                        e.getGramm(),
                        e.getSpre()
                    )
                ))
                .orElse(List.of());

        List<NoteResponse> toutesNotes =
            Stream.concat(hebdos.stream(), endNotes.stream()).toList();

        // ================== RÉSULTAT ==================
        Resultat resultat =
            resultatRepo
                .findByEleve_IdAndNiveau_Id(eleveId, niveauId)
                .orElseThrow(() ->
                    new IllegalStateException("Résultat non calculé"));

        Double soutenance =
            (codeCourt.equals("A2") || codeCourt.equals("B2"))
                ? resultat.getSoutenance()
                : null;

        return new BulletinDTO(
            new EleveResponse(
                eleve.getId(),
                eleve.getNom(),
                eleve.getPrenom(),
                eleve.getDateNaiss(),
                eleve.getLieuNaiss(),
                eleve.getNiveauScolaire(),
                eleve.getTypeProcedure(),
                eleve.getMontantProcedure(),
                eleve.getTelCandidat(),
                eleve.getTelParent(),
                eleve.getPartenaire().getNomPartenaire(),
                niveauCode,
                eleve.getRentree() != null
                    ? eleve.getRentree().getNomRentree()
                    : null
            ),
            niveauCode,
            eleve.getRentree() != null
                ? eleve.getRentree().getNomRentree()
                : null,
            toutesNotes,
            soutenance,
            resultat.getMoyenneGenerale(),
            resultat.isAdmis()
        );
    }
}
