package FLCS.GESTION.Services;

import java.util.List;

import FLCS.GESTION.Entitees.Eleve;

public interface EleveService {

    List<Eleve> findByNiveau(Long id);

}
