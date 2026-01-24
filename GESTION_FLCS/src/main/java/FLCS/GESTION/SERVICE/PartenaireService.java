package FLCS.GESTION.SERVICE;

import FLCS.GESTION.ENTITEES.Partenaire;
import FLCS.GESTION.REPOSITORY.PartenaireRepository;

import FLCS.GESTION.DTO.PartenaireResponse;
import FLCS.GESTION.DTO.PartenaireRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PartenaireService {

    private final PartenaireRepository partenaireRepository;

    public PartenaireService(PartenaireRepository partenaireRepository) {
        this.partenaireRepository = partenaireRepository;
    }

    public PartenaireResponse create(PartenaireRequest request) {

        if (partenaireRepository.existsByNomPartenaire(request.nomPartenaire())) {
            throw new IllegalArgumentException("Ce partenaire existe déjà");
        }

        Partenaire partenaire = Partenaire.builder()
                .nomPartenaire(request.nomPartenaire())
                .build();

        return mapToResponse(partenaireRepository.save(partenaire));
    }

    public List<PartenaireResponse> getAll() {
        return partenaireRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public PartenaireResponse getById(Long id) {
        Partenaire partenaire = partenaireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Partenaire introuvable"));
        return mapToResponse(partenaire);
    }

    private PartenaireResponse mapToResponse(Partenaire p) {
        return new PartenaireResponse(p.getId(), p.getNomPartenaire());
    }
}
