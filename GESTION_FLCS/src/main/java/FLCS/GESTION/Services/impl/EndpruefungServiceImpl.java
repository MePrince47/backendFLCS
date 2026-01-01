// service/impl/EndpruefungServiceImpl.java
package FLCS.GESTION.Services.impl;

import FLCS.GESTION.Dtos.Request.EndpruefungRequest;
import FLCS.GESTION.Dtos.response.EndpruefungResponse;
import FLCS.GESTION.Entitees.Endpruefung;
import FLCS.GESTION.Entitees.Eleve;
import FLCS.GESTION.Entitees.Enseignant;
import FLCS.GESTION.Entitees.Niveau;
import FLCS.GESTION.Repository.EleveRepository;
import FLCS.GESTION.Repository.EndpruefungRepository;
import FLCS.GESTION.Repository.EnseignantRepository;
import FLCS.GESTION.Repository.NiveauRepository;

import FLCS.GESTION.Services.EndpruefungService;
import FLCS.GESTION.Services.EvaluationHebdomadaireService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EndpruefungServiceImpl implements EndpruefungService {

    private final EndpruefungRepository endpruefungRepository;
    private final EleveRepository eleveRepository;
    private final NiveauRepository niveauRepository;
    private final EnseignantRepository enseignantRepository;
    private final EvaluationHebdomadaireService evaluationHebdoService;

    @Override
    @Transactional
    public EndpruefungResponse creerEndpruefung(EndpruefungRequest request) {
        log.info("Création Endprüfung - Élève: {}, Niveau: {}",
                request.getEleveId(), request.getNiveauId());

        // Vérifier si un Endprüfung existe déjà pour cet élève dans ce niveau
        if (endpruefungRepository.findByEleveIdAndNiveauId(
                request.getEleveId(), request.getNiveauId()).isPresent()) {
            throw new IllegalArgumentException(
                    "Un examen final existe déjà pour cet élève dans ce niveau");
        }
        // Récupérer les entités
        Eleve eleve = eleveRepository.findById(request.getEleveId())
                .orElseThrow(() -> new IllegalArgumentException("Élève non trouvé"));

        Niveau niveau = niveauRepository.findById(request.getNiveauId())
                .orElseThrow(() -> new IllegalArgumentException("Niveau non trouvé"));

        // Vérifier que l'élève est bien dans ce niveau
        if (!eleve.getNiveau().getId().equals(niveau.getId())) {
            throw new IllegalArgumentException(
                    "L'élève n'est pas inscrit dans le niveau spécifié");
        }

        // Créer l'Endprüfung
        Endpruefung endpruefung = new Endpruefung();
        endpruefung.setEleve(eleve);
        endpruefung.setNiveau(niveau);
        if (request.getObservateurId() != null) {
            Enseignant observateur = enseignantRepository.findById(request.getObservateurId())
                    .orElseThrow(() -> new IllegalArgumentException("Observateur non trouvé"));
            endpruefung.setObservateur(observateur);
        }

        endpruefung.setDateExamen(request.getDateExamen());
        endpruefung.setHeureDebut(request.getHeureDebut());
        endpruefung.setHeureFin(request.getHeureFin());
        endpruefung.setNoteLesen(request.getNoteLesen());
        endpruefung.setNoteHoren(request.getNoteHoren());
        endpruefung.setNoteSchreiben(request.getNoteSchreiben());
        endpruefung.setNoteGrammatik(request.getNoteGrammatik());
        endpruefung.setNoteSprechen(request.getNoteSprechen());
        // endpruefung.setCommentaire(request.getCommentaire());

        // Calculer les moyennes
        endpruefung.calculerMoyenneExamen();

        // Calculer la moyenne hebdo globale pour cet élève
        Double moyenneHebdoGlobale = evaluationHebdoService
                .calculerMoyenneHebdoGlobale(eleve.getId(), niveau.getId());

        // Calculer la moyenne finale
        endpruefung.calculerMoyenneFinale(moyenneHebdoGlobale);

        Endpruefung savedEndpruefung = endpruefungRepository.save(endpruefung);

        log.info("Endprüfung créé avec succès - ID: {}", savedEndpruefung.getId());
        return mapToResponse(savedEndpruefung);
    }

    @Override
    @Transactional
    public EndpruefungResponse mettreAJourEndpruefung(Long id, EndpruefungRequest request) {
        Endpruefung endpruefung = endpruefungRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Endprüfung non trouvé"));

        // Vérifier les nouvelles données
        if (!endpruefung.getEleve().getId().equals(request.getEleveId()) ||
                !endpruefung.getNiveau().getId().equals(request.getNiveauId())) {
        }

        // Mettre à jour les données de base
        if (!endpruefung.getEleve().getId().equals(request.getEleveId())) {
            Eleve nouvelEleve = eleveRepository.findById((Long) request.getEleveId())
                    .orElseThrow(() -> new IllegalArgumentException("Élève non trouvé"));
            endpruefung.setEleve(nouvelEleve);
        }

        if (!endpruefung.getNiveau().getId().equals(request.getNiveauId())) {
            Niveau nouveauNiveau = niveauRepository.findById((Long) request.getNiveauId())
                    .orElseThrow(() -> new IllegalArgumentException("Niveau non trouvé"));
            endpruefung.setNiveau(nouveauNiveau);
        }

        // Mettre à jour les autres champs
        endpruefung.setDateExamen(request.getDateExamen());
        endpruefung.setHeureDebut(request.getHeureDebut());
        endpruefung.setHeureFin(request.getHeureFin());
        endpruefung.setNoteLesen(request.getNoteLesen());
        endpruefung.setNoteHoren(request.getNoteHoren());
        endpruefung.setNoteSchreiben(request.getNoteSchreiben());
        endpruefung.setNoteGrammatik(request.getNoteGrammatik());
        endpruefung.setNoteSprechen(request.getNoteSprechen());

        // Recalculer les moyennes
        endpruefung.calculerMoyenneExamen();
        Double moyenneHebdoGlobale = evaluationHebdoService
                .calculerMoyenneHebdoGlobale(endpruefung.getEleve().getId(), endpruefung.getNiveau().getId());
        endpruefung.calculerMoyenneFinale(moyenneHebdoGlobale);

        Endpruefung updatedEndpruefung = endpruefungRepository.save(endpruefung);
        log.info("Endprüfung mis à jour - ID: {}", id);

        return mapToResponse(updatedEndpruefung);
    }

    private EndpruefungResponse mapToResponse(Endpruefung updatedEndpruefung) {
        if (updatedEndpruefung == null)
            return null;
        EndpruefungResponse r = new EndpruefungResponse();
        r.setId(updatedEndpruefung.getId());
        r.setUuid(updatedEndpruefung.getUuid());
        if (updatedEndpruefung.getEleve() != null) {
            r.setEleveId(updatedEndpruefung.getEleve().getId());
            r.setEleveNom(updatedEndpruefung.getEleve().getNom() + " " + updatedEndpruefung.getEleve().getPrenom());
            r.setEleveMatricule(updatedEndpruefung.getEleve().getMatricule());
        }
        if (updatedEndpruefung.getNiveau() != null) {
            r.setNiveauId(updatedEndpruefung.getNiveau().getId());
            r.setNiveauNom(updatedEndpruefung.getNiveau().getNom());
        }
        if (updatedEndpruefung.getObservateur() != null) {
            r.setEnseignantId(updatedEndpruefung.getObservateur().getId());
            r.setEnseignantNom(updatedEndpruefung.getObservateur().getNom());
        }
        r.setDateEvaluation(updatedEndpruefung.getDateExamen());
        r.setNoteLesen(updatedEndpruefung.getNoteLesen());
        r.setNoteHoren(updatedEndpruefung.getNoteHoren());
        r.setNoteSchreiben(updatedEndpruefung.getNoteSchreiben());
        r.setNoteGrammatik(updatedEndpruefung.getNoteGrammatik());
        r.setNoteSprechen(updatedEndpruefung.getNoteSprechen());
        
        // preferer la moyenne finale si disponible, sinon la moyenne de l'examen
        r.setMoyenne(updatedEndpruefung.getMoyenneFinale() != null ? updatedEndpruefung.getMoyenneFinale()
                : updatedEndpruefung.getMoyenneExamen());
        if (updatedEndpruefung.getResultat() != null)
            r.setStatut(updatedEndpruefung.getResultat().name());
        r.setDateCreation(updatedEndpruefung.getDateCreation());
        r.setDateModification(updatedEndpruefung.getDateModification());
        return r;
    }

    @Override
    public List<EndpruefungResponse> getEndpruefungenByResultat(String resultat) {
        Endpruefung.Resultat resultatEnum;
        try {
            resultatEnum = Endpruefung.Resultat.valueOf(resultat.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Résultat invalide: " + resultat);
        }

        return endpruefungRepository.findByResultat(resultatEnum).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EndpruefungResponse validerEndpruefung(Long id) {
        Endpruefung e = endpruefungRepository.findById(id).orElse(null);
        if (e == null)
            throw new IllegalArgumentException("Endpruefung non trouvée: " + id);
        // recalculer les moyennes et déterminer le résultat
        e.calculateMoyennes();
        Double moyenneHebdo = evaluationHebdoService.calculerMoyenneHebdoGlobale(e.getEleve().getId(),
                e.getNiveau().getId());
        e.calculerMoyenneFinale(moyenneHebdo);
        if (e.getMoyenneFinale() != null) {
            e.setResultat(e.getMoyenneFinale() >= 10 ? Endpruefung.Resultat.ADMIS : Endpruefung.Resultat.AJOURNE);
        }
        Endpruefung saved = endpruefungRepository.save(e);
        return mapToResponse(saved);
    }

    @Override
    public EndpruefungResponse corrigerEndpruefung(Long id, EndpruefungRequest request) {
        Endpruefung e = endpruefungRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Endpruefung non trouvée"));
        // Mettre à jour les notes si fournies
        if (request.getNoteLesen() != null)
            e.setNoteLesen(request.getNoteLesen());
        if (request.getNoteHoren() != null)
            e.setNoteHoren(request.getNoteHoren());
        if (request.getNoteSchreiben() != null)
            e.setNoteSchreiben(request.getNoteSchreiben());
        if (request.getNoteGrammatik() != null)
            e.setNoteGrammatik(request.getNoteGrammatik());
        if (request.getNoteSprechen() != null)
            e.setNoteSprechen(request.getNoteSprechen());

        // Recalculer
        e.calculerMoyenneExamen();
        Double moyenneHebdo = evaluationHebdoService.calculerMoyenneHebdoGlobale(e.getEleve().getId(),
                e.getNiveau().getId());
        e.calculerMoyenneFinale(moyenneHebdo);
        Endpruefung saved = endpruefungRepository.save(e);
        return mapToResponse(saved);
    }

    @Override
    public void annulerEndpruefung(Long id) {
        Endpruefung e = endpruefungRepository.findById(id).orElse(null);
        if (e == null)
            return;
        e.setResultat(Endpruefung.Resultat.ABSENT);
        endpruefungRepository.save(e);
    }

    @Override
    public void supprimerEndpruefung(Long id) {
        endpruefungRepository.deleteById(id);
    }

    @Override
    public EndpruefungResponse getEndpruefungById(Long id) {
        Endpruefung e = endpruefungRepository.findById(id).orElse(null);
        return mapToResponse(e);
    }

    @Override
    public List<EndpruefungResponse> getEndpruefungenByEleve(Long eleveId) {
        return endpruefungRepository.findByEleveId(eleveId).stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EndpruefungResponse> getEndpruefungenByNiveau(Long niveauId) {
        return endpruefungRepository.findByNiveauId(niveauId).stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EndpruefungResponse getEndpruefungByEleveAndNiveau(Long eleveId, Long niveauId) {
        Endpruefung e = endpruefungRepository.findByEleveIdAndNiveauId(eleveId, niveauId).orElse(null);
        return mapToResponse(e);
    }

    @Override
    public EndpruefungResponse proclamerResultat(Long id) {
        // Proclamer le résultat : même logique que valider
        return validerEndpruefung(id);
    }

    @Override
    public List<EndpruefungResponse> getEndpruefungensManquantes(Long niveauId, Integer semaine, Integer annee) {
        // L'entité Endpruefung ne contient pas les informations de semaine/année.
        // Retourner une liste vide pour l'instant.
        return List.of();
    }

    @Override
    public List<EndpruefungResponse> getEndpruefungenNonRemplies(Long niveauId, Integer semaine, Integer annee) {
        // Idem: pas d'informations disponibles dans l'entité
        return List.of();
    }

    @Override
    public double calculerMoyenneFinale(Long endpruefungId, Long niveauId) {
        Endpruefung e = endpruefungRepository.findById(endpruefungId).orElse(null);
        if (e == null)
            return 0.0;
        Double moyenneHebdo = evaluationHebdoService.calculerMoyenneHebdoGlobale(e.getEleve().getId(), niveauId);
        e.calculerMoyenneFinale(moyenneHebdo);
        endpruefungRepository.save(e);
        return e.getMoyenneFinale() != null ? e.getMoyenneFinale() : 0.0;
    }

}