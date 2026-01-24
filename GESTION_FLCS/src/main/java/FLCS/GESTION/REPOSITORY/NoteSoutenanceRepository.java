package FLCS.GESTION.REPOSITORY;

import FLCS.GESTION.ENTITEES.NoteSoutenance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoteSoutenanceRepository
        extends JpaRepository<NoteSoutenance, Long> {

    Optional<NoteSoutenance> findByEleve_IdAndNiveau_Id(
        Long eleveId, Long niveauId
    );
}
