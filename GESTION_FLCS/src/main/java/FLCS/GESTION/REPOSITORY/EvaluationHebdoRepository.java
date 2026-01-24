package FLCS.GESTION.REPOSITORY;

import FLCS.GESTION.ENTITEES.EvaluationHebdo;
import FLCS.GESTION.ENTITEES.Niveau;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvaluationHebdoRepository extends JpaRepository<EvaluationHebdo, Long> {

    List<EvaluationHebdo> findByNiveauOrderBySemaineNumAsc(Niveau niveau);

    boolean existsByNiveau(Niveau niveau);
}
