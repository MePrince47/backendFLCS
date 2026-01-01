package FLCS.GESTION.Repository;

import FLCS.GESTION.Entitees.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long> {

    Optional<Paiement> findByReference(String reference);

    List<Paiement> findByEleveId(Long eleveId);

    List<Paiement> findByPartenaireId(Long partenaireId);

    List<Paiement> findByDatePaiementBetween(LocalDate start, LocalDate end);

    @Query("SELECT SUM(p.montant) FROM Paiement p WHERE p.eleve.id = :eleveId AND p.statut = 'VALIDE'")
    Double calculateTotalPayeByEleveId(Long eleveId);

    @Query("SELECT SUM(p.montant) FROM Paiement p WHERE p.datePaiement BETWEEN :start AND :end")
    Double calculateTotalPeriode(LocalDate start, LocalDate end);
}