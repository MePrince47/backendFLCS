package FLCS.GESTION.REPOSITORY;

import FLCS.GESTION.ENTITEES.Rentree;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RentreeRepository extends JpaRepository<Rentree, Long> {

    // Vérifier l’unicité métier d’une rentrée
    Optional<Rentree> findByNomRentree(String nomRentree);

    // vérifier si une rentrée existe déjà
    boolean existsByNomRentree(String nomRentree);
}
