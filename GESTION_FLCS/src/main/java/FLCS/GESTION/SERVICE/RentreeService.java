package FLCS.GESTION.SERVICE;

import FLCS.GESTION.ENTITEES.Niveau;
import FLCS.GESTION.ENTITEES.Rentree;
import FLCS.GESTION.REPOSITORY.RentreeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RentreeService {

    private final RentreeRepository rentreeRepo;

    public RentreeService(RentreeRepository rentreeRepo) {
        this.rentreeRepo = rentreeRepo;
    }

    public Rentree createRentree(Rentree rentree) {

        Rentree saved = rentreeRepo.save(rentree);

        List<String> codes = List.of("A1", "A2", "B1", "B2");

        for (String code : codes) {
            Niveau niveau = Niveau.builder()
                    .code(code)
                    .build();

            saved.addNiveau(niveau);
        }

        return rentreeRepo.save(saved);
    }

    // 🔴 CETTE MÉTHODE MANQUAIT
    public List<Rentree> getAllRentrees() {
        return rentreeRepo.findAll();
    }
}


