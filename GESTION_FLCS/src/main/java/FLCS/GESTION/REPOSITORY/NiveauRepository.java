package FLCS.GESTION.REPOSITORY;

import FLCS.GESTION.ENTITEES.Niveau;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NiveauRepository extends JpaRepository<Niveau, Long> {

    // Un niveau précis dans une rentrée donnée
    Optional<Niveau> findByCodeAndRentree_NomRentree(
        String code,
        String nomRentree
    );

    Optional<Niveau> findByCodeAndRentreeIsNull(String code);
    Optional<Niveau> findByCode(String code);


    // Tous les niveaux d’une rentrée
    List<Niveau> findByRentree_Id(Long rentreeId);

    // Tous les niveaux indépendants (hors rentrée)
    List<Niveau> findByRentreeIsNull();

    // Vérifier si un niveau existe déjà dans une rentrée
    boolean existsByCodeAndRentree_Id(String code, Long rentreeId);
}
