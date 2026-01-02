package FLCS.GESTION.REPOSITORY;

import FLCS.GESTION.ENTITEES.Eleve;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface EleveRepository extends JpaRepository<Eleve, Long> {

    @Query("""
    SELECT e FROM Eleve e
    WHERE (:nom IS NULL OR LOWER(e.nom) LIKE LOWER(CONCAT('%', :nom, '%')))
    AND (:niveauScolaire IS NULL OR e.niveauScolaire = :niveauScolaire)
    AND (:rentree IS NULL OR e.rentree.nomRentree = :rentree)
    AND (:niveauLangue IS NULL OR e.niveauLangue.code = :niveauLangue)
    AND (:partenaire IS NULL OR e.partenaire.nomPartenaire = :partenaire)
    """)
    List<Eleve> rechercheAvancee(
        @Param("nom") String nom,
        @Param("niveauScolaire") String niveauScolaire,
        @Param("rentree") String rentree,
        @Param("niveauLangue") String niveauLangue,
        @Param("partenaire") String partenaire
    );

    List<Eleve> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(
        String nom,
        String prenom
    );

    List<Eleve> findByNiveauLangue_Code(String code);



}

