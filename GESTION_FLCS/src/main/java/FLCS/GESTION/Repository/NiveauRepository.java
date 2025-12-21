package FLCS.GESTION.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import FLCS.GESTION.Models.Niveau;

import java.util.List;
import java.util.Optional;

@Repository
public interface NiveauRepository extends JpaRepository<Niveau, Long> {
    
    List<Niveau> findByRentreeId(Long rentreeId);
    
    List<Niveau> findByNom(String nom);
    
    Optional<Niveau> findByRentreeIdAndNom(Long rentreeId, String nom);
    
    List<Niveau> findByEnseignantId(Long enseignantId);
    
    @Query("SELECT n FROM Niveau n WHERE n.rentree.id = :rentreeId AND n.statut = 'EN_COURS'")
    List<Niveau> findNiveauxEnCoursByRentreeId(Long rentreeId);
    
    @Query("SELECT COUNT(e) FROM Eleve e WHERE e.niveau.id = :niveauId")
    Long countElevesByNiveauId(Long niveauId);
}