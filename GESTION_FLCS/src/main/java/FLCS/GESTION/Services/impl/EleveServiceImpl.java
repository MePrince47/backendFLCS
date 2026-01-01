package FLCS.GESTION.Services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import FLCS.GESTION.Entitees.Eleve;
import FLCS.GESTION.Repository.EleveRepository;
import FLCS.GESTION.Services.EleveService;
import lombok.RequiredArgsConstructor;

/**
 * Implémentation minimale de `EleveService`.
 * Délègue les opérations de lecture au repository.
 */
@Service
@RequiredArgsConstructor
public class EleveServiceImpl implements EleveService {

    private final EleveRepository eleveRepository;

    @Override
    public List<Eleve> findByNiveau(Long id) {
        return eleveRepository.findByNiveauId(id);
    }

}
