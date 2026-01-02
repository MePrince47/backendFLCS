package FLCS.GESTION.REPOSITORY;

import FLCS.GESTION.ENTITEES.Partenaire;
import FLCS.GESTION.DTO.EleveRequest;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartenaireRepository extends JpaRepository<Partenaire, Long> {
    Optional<Partenaire> findByNomPartenaire(String nomPartenaire);

    boolean existsByNomPartenaire(String nomPartenaire);
}

