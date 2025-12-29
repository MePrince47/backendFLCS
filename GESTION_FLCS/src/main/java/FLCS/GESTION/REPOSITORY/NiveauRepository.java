package FLCS.GESTION.REPOSITORY;

import FLCS.GESTION.ENTITEES.Niveau;
import FLCS.GESTION.DTO.EleveRequest;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NiveauRepository extends JpaRepository<Niveau, Long> {

    Optional<Niveau> findByCodeAndRentree_NomRentree(
        String code,
        String nomRentree
    );
}
