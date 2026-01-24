package FLCS.GESTION.REPOSITORY;

import FLCS.GESTION.ENTITEES.Utilisateur;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    Optional<Utilisateur> findByUsername(String username);
}
