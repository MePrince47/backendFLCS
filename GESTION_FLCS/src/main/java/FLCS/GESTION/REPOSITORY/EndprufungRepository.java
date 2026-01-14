package FLCS.GESTION.REPOSITORY;

import FLCS.GESTION.ENTITEES.Endprufung;
import FLCS.GESTION.ENTITEES.Niveau;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EndprufungRepository 
        extends JpaRepository<Endprufung, Long> {

    Optional<Endprufung> findByNiveau_Id(Long niveauId);
    
    boolean existsByNiveau(Niveau niveau);

}

