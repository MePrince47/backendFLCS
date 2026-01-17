package FLCS.GESTION.REPOSITORY;

import FLCS.GESTION.ENTITEES.Paiement;
import FLCS.GESTION.DTO.EleveRequest;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {

    List<Paiement> findByEleve_Id(Long eleveId);

    boolean existsByReferenceVirement(String referenceVirement);

    // HISTORIQUE DES PAIEMENTS PAR ELEVE
    List<Paiement> findByEleveIdOrderByDatePaiementDesc(Long eleveId);

    // CALCUL DU TOTAL PAYE
    @Query("""
        SELECT COALESCE(SUM(p.montant), 0)
        FROM Paiement p
        WHERE p.eleve.id = :eleveId
    """)
    Double totalPayeParEleve(@Param("eleveId") Long eleveId);
}
