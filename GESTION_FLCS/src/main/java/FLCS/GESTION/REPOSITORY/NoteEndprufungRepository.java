package FLCS.GESTION.REPOSITORY;

import FLCS.GESTION.ENTITEES.NoteEndprufung;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoteEndprufungRepository
        extends JpaRepository<NoteEndprufung, Long> {

    //  Unicité : ajout / modification
    Optional<NoteEndprufung> findByEndprufung_IdAndEleve_Id(
            Long endprufungId,
            Long eleveId
    );

    //  Résultats finaux d’un niveau
    List<NoteEndprufung> findByEndprufung_Niveau_Id(Long niveauId);

    //  Résultat final d’un élève pour un niveau donné
    Optional<NoteEndprufung> findByEleve_IdAndEndprufung_Niveau_Id(
            Long eleveId,
            Long niveauId
    );
}
