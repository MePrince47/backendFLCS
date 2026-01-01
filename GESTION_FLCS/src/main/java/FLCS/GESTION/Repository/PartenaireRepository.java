package FLCS.GESTION.Repository;

import FLCS.GESTION.Entitees.Partenaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartenaireRepository extends JpaRepository<Partenaire, Long> {

    Optional<Partenaire> findByNom(String nom);

    List<Partenaire> findByNomContainingIgnoreCase(String nom);

    List<Partenaire> findByType(Partenaire.Type type);

    List<Partenaire> findByStatut(Partenaire.Statut statut);
}