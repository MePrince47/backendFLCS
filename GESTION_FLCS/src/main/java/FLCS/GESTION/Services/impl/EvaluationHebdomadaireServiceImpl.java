package FLCS.GESTION.Services.impl;

import FLCS.GESTION.Dtos.Request.*;
import FLCS.GESTION.Dtos.response.*;
import FLCS.GESTION.Mappers.EvaluationHebdomadaireMapper;
import FLCS.GESTION.Entitees.*;
import FLCS.GESTION.Repository.*;
import FLCS.GESTION.Services.EvaluationHebdomadaireService;
// import FLCS.GESTION.Exceptions.ResourceNotFoundException;   

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.management.relation.RelationNotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class EvaluationHebdomadaireServiceImpl implements EvaluationHebdomadaireService {

    private final EvaluationHebdomadaireRepository evaluationRepository;
    private final EleveRepository eleveRepository;
    private final NiveauRepository niveauRepository;
    private final EnseignantRepository enseignantRepository;
    // private final EvaluationHebdomadaireResponse evaluationHebdomadaireResponse;
    // private final EvaluationHebdomadaireRequest evaluationHebdomadaireRequest;

    @SuppressWarnings("unused")
    @Override
    @Transactional
    public EvaluationHebdomadaireResponse creerEvaluation(EvaluationHebdomadaireRequest request) {
        log.info("Création d'une évaluation hebdomadaire pour l'élève {}", request.getEleveId());

        // Vérifier si une évaluation existe déjà pour cette semaine
        if (existeEvaluationPourSemaine(request.getEleveId(), request.getNiveauId(),
                request.getSemaine(), request.getAnnee())) {
            throw new IllegalArgumentException(
                    String.format("Une évaluation existe déjà pour cet élève en semaine %d/%d",
                            request.getSemaine(), request.getAnnee()));
        }

        // Récupérer les entités
        Eleve eleve = eleveRepository.findById(request.getEleveId())
                .orElseThrow(() -> new IllegalArgumentException("Élève non trouvé"));

        Niveau niveau = niveauRepository.findById(request.getNiveauId())
                .orElseThrow(() -> new IllegalArgumentException("Niveau non trouvé"));

        Enseignant enseignant = enseignantRepository.findById(request.getEnseignantId())
                .orElseThrow(() -> new IllegalArgumentException("Enseignant non trouvé"));

        // Créer l'évaluation
        EvaluationHebdomadaire evaluation = new EvaluationHebdomadaire();

        // Si toutes les notes sont remplies, marquer comme SAISI
        if (sontToutesNotesRemplies(evaluation)) {
            evaluation.setStatut(EvaluationHebdomadaire.Statut.SAISI);
        }

        EvaluationHebdomadaire savedEvaluation = evaluationRepository.save(evaluation);
        log.info("Évaluation hebdomadaire créée avec ID: {}", savedEvaluation.getId());

        return toResponse(savedEvaluation);
    }

    private boolean sontToutesNotesRemplies(EvaluationHebdomadaire evaluation) {
        return evaluation.getNoteLesen() != null &&
                evaluation.getNoteHoren() != null &&
                evaluation.getNoteSchreiben() != null &&
                evaluation.getNoteGrammatik() != null &&
                evaluation.getNoteSprechen() != null;
    }

    @Override
    @Transactional
    public EvaluationHebdomadaireResponse mettreAJourEvaluation(Long id, EvaluationHebdomadaireRequest request) {
        EvaluationHebdomadaire evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Évaluation non trouvée"));

        // Vérifier que l'évaluation peut être modifiée
        if (!evaluation.getStatut().equals(EvaluationHebdomadaire.Statut.BROUILLON) &&
                !evaluation.getStatut().equals(EvaluationHebdomadaire.Statut.SAISI)) {
            throw new IllegalStateException("Cette évaluation ne peut plus être modifiée");
        }

        // Mettre à jour les notes
        evaluation.setNoteLesen(request.getNoteLesen());
        evaluation.setNoteHoren(request.getNoteHoren());
        evaluation.setNoteSchreiben(request.getNoteSchreiben());
        evaluation.setNoteGrammatik(request.getNoteGrammatik());
        evaluation.setNoteSprechen(request.getNoteSprechen());

        // Si toutes les notes sont maintenant remplies, passer en SAISI
        if (sontToutesNotesRemplies(evaluation) &&
                evaluation.getStatut().equals(EvaluationHebdomadaire.Statut.BROUILLON)) {
            evaluation.setStatut(EvaluationHebdomadaire.Statut.SAISI);
        }

        EvaluationHebdomadaire updatedEvaluation = evaluationRepository.save(evaluation);
        log.info("Évaluation mise à jour: {}", id);

        return toResponse(updatedEvaluation);
    }

    @Override
    @Transactional
    public EvaluationHebdomadaireResponse validerEvaluation(Long id) {
        EvaluationHebdomadaire evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Évaluation non trouvée"));

        // Vérifier que l'évaluation peut être validée
        if (!evaluation.getStatut().equals(EvaluationHebdomadaire.Statut.SAISI)) {
            throw new IllegalStateException("Seules les évaluations saisies peuvent être validées");
        }

        evaluation.setStatut(EvaluationHebdomadaire.Statut.VALIDE);
        evaluation.setDateEvaluation(LocalDate.now());

        EvaluationHebdomadaire validatedEvaluation = evaluationRepository.save(evaluation);
        log.info("Évaluation validée: {}", id);

        return toResponse(validatedEvaluation);
    }

    @Override
    @Transactional
    public void annulerEvaluation(Long id) {
        EvaluationHebdomadaire evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Évaluation non trouvée"));

        evaluation.setStatut(EvaluationHebdomadaire.Statut.ANNULE);

        evaluationRepository.save(evaluation);
        log.info("Évaluation annulée: {}", id);
    }

    @Override
    @Transactional
    public void supprimerEvaluation(Long id) {
        EvaluationHebdomadaire evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Évaluation non trouvée"));

        // Vérifier que l'évaluation peut être supprimée
        if (evaluation.getStatut().equals(EvaluationHebdomadaire.Statut.VALIDE)) {
            throw new IllegalStateException("Une évaluation validée ne peut pas être supprimée");
        }

        evaluationRepository.delete(evaluation);
        log.info("Évaluation supprimée: {}", id);
    }

    @Override
    public EvaluationHebdomadaireResponse getEvaluationById(Long id) {
        EvaluationHebdomadaire evaluation = null;
        try {
            evaluation = evaluationRepository.findById(id)
                    .orElseThrow(() -> new RelationNotFoundException("Évaluation non trouvée"));
        } catch (RelationNotFoundException e) {

            e.printStackTrace();
        }
        return toResponse(evaluation);
    }

    @Override
    public List<EvaluationHebdomadaireResponse> getEvaluationsByEleve(Long eleveId) {
        return evaluationRepository.findByEleveId(eleveId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EvaluationHebdomadaireResponse> getEvaluationsByNiveau(Long niveauId) {
        return evaluationRepository.findByNiveauId(niveauId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EvaluationHebdomadaireResponse> getEvaluationsByEnseignant(Long enseignantId) {
        return evaluationRepository.findByEnseignantId(enseignantId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EvaluationHebdomadaireResponse> getEvaluationsByNiveauEtSemaine(Long niveauId, Integer semaine,
            Integer annee) {
        return evaluationRepository.findByNiveauAndSemaine(niveauId, semaine, annee).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Double calculerMoyenneHebdoGlobale(Long eleveId, Long niveauId) {
        Double moyenne = evaluationRepository.calculateMoyenneGlobale(eleveId, niveauId);
        return moyenne != null ? moyenne : 0.0;
    }

    @Override
    public boolean existeEvaluationPourSemaine(Long eleveId, Long niveauId, Integer semaine, Integer annee) {
        return evaluationRepository.existsByEleveIdAndNiveauIdAndSemaineAndAnnee(
                eleveId, niveauId, semaine, annee);
    }

    private EvaluationHebdomadaireResponse toResponse(EvaluationHebdomadaire evaluation) {
        EvaluationHebdomadaireResponse response = new EvaluationHebdomadaireResponse();
        response.setId(evaluation.getId());
        response.setUuid(evaluation.getUuid());
        response.setEleveId(evaluation.getEleve().getId());
        response.setEleveNom(evaluation.getEleve().getNom() + " " + evaluation.getEleve().getPrenom());
        response.setNiveauNom(evaluation.getNiveau().getNom());
        response.setNoteLesen(evaluation.getNoteLesen());
        response.setNoteHoren(evaluation.getNoteHoren());
        response.setNoteSchreiben(evaluation.getNoteSchreiben());
        response.setNoteGrammatik(evaluation.getNoteGrammatik());
        response.setNoteSprechen(evaluation.getNoteSprechen());

        return response;
    }

    public static org.slf4j.Logger getLog() {
        return log;
    }

    public EvaluationHebdomadaireRepository getEvaluationRepository() {
        return evaluationRepository;
    }

    public EleveRepository getEleveRepository() {
        return eleveRepository;
    }

    public NiveauRepository getNiveauRepository() {
        return niveauRepository;
    }

    public EnseignantRepository getEnseignantRepository() {
        return enseignantRepository;
    }

    @Override
    public List<EvaluationHebdomadaireResponse> getEvaluationsManquantes(Long niveauId, Integer semaine,
            Integer annee) {
        // Récupérer tous les élèves du niveau
        List<Eleve> eleves = eleveRepository.findByNiveauId(niveauId);

        // Pour chaque élève, vérifier si une évaluation existe pour la
        // semaine/année/niveau
        return eleves.stream()
                .filter(e -> !evaluationRepository
                        .findByEleveIdAndSemaineAndAnneeAndNiveauId(e.getId(), semaine, annee, niveauId)
                        .isPresent())
                .map(e -> {
                    EvaluationHebdomadaireResponse resp = new EvaluationHebdomadaireResponse();
                    resp.setId(null);
                    resp.setUuid(null);
                    resp.setEleveId(e.getId());
                    resp.setEleveNom(e.getNom() + " " + e.getPrenom());
                    resp.setNiveauNom(e.getNiveau() != null ? e.getNiveau().getNom() : null);
                    resp.setNoteLesen(null);
                    resp.setNoteHoren(null);
                    resp.setNoteSchreiben(null);
                    resp.setNoteGrammatik(null);
                    resp.setNoteSprechen(null);
                    return resp;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<EvaluationHebdomadaireResponse> getEvaluationsBySemaine(Integer semaine, Integer annee) {
        return evaluationRepository.findAll().stream()
                .filter(e -> e.getSemaine() != null && e.getAnnee() != null && e.getSemaine().equals(semaine)
                        && e.getAnnee().equals(annee))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EvaluationHebdomadaireResponse> getEvaluationsNonRemplies(Long niveauId, Integer semaine,
            Integer annee) {
        List<EvaluationHebdomadaire> evaluations = evaluationRepository.findByNiveauAndSemaine(niveauId, semaine,
                annee);

        return evaluations.stream().filter(e -> !sontToutesNotesRemplies(e)).map(this::toResponse)
                .collect(Collectors.toList());
    }

}
