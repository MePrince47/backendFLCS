package FLCS.GESTION.REPOSITORY;

import FLCS.GESTION.ENTITEES.NoteHebdo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoteHebdoRepository 
        extends JpaRepository<NoteHebdo, Long> {

    //  Unicité métier
    Optional<NoteHebdo> findByEvaluationHebdo_IdAndEleve_Id(
        Long evaluationId,
        Long eleveId
    );

    //  Consultation : toutes les notes d’un niveau
    List<NoteHebdo> findByEvaluationHebdo_Niveau_Id(Long niveauId);

    // Toutes les notes hebdo d’un élève pour un niveau donné
    List<NoteHebdo> findByEleve_IdAndEvaluationHebdo_Niveau_Id(
            Long eleveId,
            Long niveauId
    );

    // Notes d’un niveau pour une semaine précise
    List<NoteHebdo> findByEvaluationHebdo_Niveau_IdAndEvaluationHebdo_SemaineNum(
        Long niveauId,
        Integer semaineNum
);

}
