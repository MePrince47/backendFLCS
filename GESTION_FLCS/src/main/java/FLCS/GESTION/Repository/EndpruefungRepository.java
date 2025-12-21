package FLCS.GESTION.Repository;
import FLCS.GESTION.Models.Endpruefung;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EndpruefungRepository extends JpaRepository<Endpruefung, Long> {
    
    List<Endpruefung> findByEleveId(Long eleveId);
    
    List<Endpruefung> findByNiveauId(Long niveauId);
    
    Optional<Endpruefung> findByEleveIdAndNiveauId(Long eleveId, Long niveauId);
    
    List<Endpruefung> findByResultat(Endpruefung.Resultat resultat);
    
    @Query("SELECT COUNT(e) FROM Endpruefung e WHERE e.niveau.id = :niveauId AND e.resultat = 'ADMIS'")
    Long countAdmisByNiveauId(Long niveauId);
    
    @Query("SELECT AVG(e.moyenneExamen) FROM Endpruefung e WHERE e.niveau.id = :niveauId")
    Double calculateMoyenneExamenByNiveau(Long niveauId);
}