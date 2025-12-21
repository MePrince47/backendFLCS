package FLCS.GESTION.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import FLCS.GESTION.Models.Rentree;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RentreeRepository extends JpaRepository<Rentree, Long> {
    
    Optional<Rentree> findByCode(String code);
    
    List<Rentree> findByStatut(Rentree.Statut statut);
    
    List<Rentree> findByDateDebutBetween(LocalDate start, LocalDate end);
    
    @Query("SELECT r FROM Rentree r WHERE r.dateDebut <= :date AND r.dateFin >= :date")
    List<Rentree> findRentreesEnCours(LocalDate date);
    
    boolean existsByCode(String code);
    
    @Query("SELECT COUNT(e) FROM Eleve e WHERE e.rentree.id = :rentreeId")
    Long countElevesByRentreeId(Long rentreeId);
}