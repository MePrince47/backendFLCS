package FLCS.GESTION.REPOSITORY;

import FLCS.GESTION.ENTITEES.Rentree;
import FLCS.GESTION.DTO.EleveRequest;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentreeRepository extends JpaRepository<Rentree, Long> {
    Optional<Rentree> findByNomRentree(String nomRentree);
}
