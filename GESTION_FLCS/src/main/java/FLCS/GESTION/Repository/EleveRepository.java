package FLCS.GESTION.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import FLCS.GESTION.Models.Eleve;

import java.util.List;
import java.util.Optional;

@Repository
public interface EleveRepository extends JpaRepository<Eleve, Long> {
    
    Optional<Eleve> findByMatricule(String matricule);
    
    List<Eleve> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom);
    
    List<Eleve> findByRentreeId(Long rentreeId);
    
    List<Eleve> findByNiveauId(Long niveauId);
    
    List<Eleve> findByPartenaireId(Long partenaireId);
    
    List<Eleve> findByStatut(Eleve.Statut statut);
    
    List<Eleve> findByStatutPaiement(Eleve.StatutPaiement statutPaiement);
    
    @Query("SELECT e FROM Eleve e WHERE e.niveau.nom = :niveau")
    List<Eleve> findByNiveauNom(String niveau);
    
    @Query("SELECT COUNT(e) FROM Eleve e WHERE e.rentree.id = :rentreeId")
    Long countByRentreeId(Long rentreeId);
    
    @Query("SELECT e FROM Eleve e WHERE e.niveauActuel = :niveau AND e.statut = 'EN_FORMATION'")
    List<Eleve> findElevesEnFormationByNiveau(String niveau);
}