package FLCS.GESTION.REPOSITORY;

import FLCS.GESTION.ENTITEES.Resultat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResultatRepository extends JpaRepository<Resultat, Long> {

    Optional<Resultat> findByEleve_IdAndNiveau_Id(Long eleveId, Long niveauId);

    List<Resultat> findByNiveau_Id(Long niveauId);

    boolean existsByEleve_IdAndNiveau_Id(Long eleveId, Long niveauId);
}
