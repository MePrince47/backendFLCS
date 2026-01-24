package FLCS.GESTION.SERVICE;

import FLCS.GESTION.ENTITEES.Eleve;
import FLCS.GESTION.REPOSITORY.EleveRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ElevePdfService {

    private final EleveRepository eleveRepository;

    public ElevePdfService(EleveRepository eleveRepository) {
        this.eleveRepository = eleveRepository;
    }

    public List<Eleve> tous() {
        return eleveRepository.findAll();
    }

    public List<Eleve> parNiveau(Long niveauId) {
        return eleveRepository.findByNiveauLangue_Id(niveauId);
    }

    public List<Eleve> parRentree(Long rentreeId) {
        return eleveRepository.findByNiveauLangue_Rentree_Id(rentreeId);
    }

    public List<Eleve> parPartenaire(String partenaire) {
        return eleveRepository.rechercheAvancee(
            null, null, null, null, partenaire
        );
    }
}
