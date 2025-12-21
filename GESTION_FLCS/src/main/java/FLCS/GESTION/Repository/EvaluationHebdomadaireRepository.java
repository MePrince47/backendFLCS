package FLCS.GESTION.Repository;

import FLCS.GESTION.Models.EvaluationHebdomadaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluationHebdomadaireRepository extends JpaRepository<EvaluationHebdomadaire, Long> {
    
    List<EvaluationHebdomadaire> findByEleveId(Long eleveId);
    
    List<EvaluationHebdomadaire> findByNiveauId(Long niveauId);
    
    List<EvaluationHebdomadaire> findByEnseignantId(Long enseignantId);
    
    Optional<EvaluationHebdomadaire> findByEleveIdAndSemaineAndAnneeAndNiveauId(
        Long eleveId, Integer semaine, Integer annee, Long niveauId);
    
    @Query("SELECT e FROM EvaluationHebdomadaire e WHERE e.niveau.id = :niveauId AND e.semaine = :semaine AND e.annee = :annee")
    List<EvaluationHebdomadaire> findByNiveauAndSemaine(Long niveauId, Integer semaine, Integer annee);
    
    @Query("SELECT AVG((e.noteLesen + e.noteHoren + e.noteSchreiben + e.noteGrammatik + e.noteSprechen) / 5) " +
           "FROM EvaluationHebdomadaire e WHERE e.eleve.id = :eleveId AND e.niveau.id = :niveauId")
    Double calculateMoyenneGlobale(Long eleveId, Long niveauId);
}