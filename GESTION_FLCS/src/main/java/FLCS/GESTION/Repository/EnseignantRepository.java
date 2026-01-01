package FLCS.GESTION.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import FLCS.GESTION.Entitees.Enseignant;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {

    Optional<Enseignant> findByMatricule(String matricule);

    Optional<Enseignant> findByEmail(String email);

    List<Enseignant> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom);

    List<Enseignant> findByStatut(Enseignant.Statut statut);

    boolean existsByEmail(String email);

    boolean existsByMatricule(String matricule);
}