package FLCS.GESTION.Services;

import java.util.List;

import FLCS.GESTION.Dtos.Request.EvaluationHebdomadaireRequest;
import FLCS.GESTION.Dtos.response.EvaluationHebdomadaireResponse;
import FLCS.GESTION.Models.EvaluationHebdomadaire;

public interface EvaluationHebdomadaireService {
    
    EvaluationHebdomadaireResponse creerEvaluation(EvaluationHebdomadaireRequest request);
    
    EvaluationHebdomadaireResponse mettreAJourEvaluation(Long id, EvaluationHebdomadaireRequest request);
    
    EvaluationHebdomadaireResponse validerEvaluation(Long id);
    
    void annulerEvaluation(Long id);
    
    void supprimerEvaluation(Long id);
    
    EvaluationHebdomadaireResponse getEvaluationById(Long id);
    
    List<EvaluationHebdomadaireResponse> getEvaluationsByEleve(Long eleveId);
    
    List<EvaluationHebdomadaireResponse> getEvaluationsByNiveau(Long niveauId);
    
    List<EvaluationHebdomadaireResponse> getEvaluationsByEnseignant(Long enseignantId);
    
    List<EvaluationHebdomadaireResponse> getEvaluationsBySemaine(Integer semaine, Integer annee);
    
    List<EvaluationHebdomadaireResponse> getEvaluationsByNiveauEtSemaine(Long niveauId, Integer semaine, Integer annee);
    
    Double calculerMoyenneHebdoGlobale(Long eleveId, Long niveauId);
    
    boolean existeEvaluationPourSemaine(Long eleveId, Long niveauId, Integer semaine, Integer annee);
    
    // StatistiquesEvaluation getStatistiquesByNiveau(Long niveauId, Integer semaine, Integer annee);
    
    List<EvaluationHebdomadaireResponse> getEvaluationsManquantes(Long niveauId, Integer semaine, Integer annee);

    List<EvaluationHebdomadaireResponse> getEvaluationsNonRemplies(Long niveauId, Integer semaine, Integer annee);
}

