package FLCS.GESTION.CONTROLLER;

import FLCS.GESTION.ENTITEES.Partenaire;
import FLCS.GESTION.REPOSITORY.PartenaireRepository;
import FLCS.GESTION.REPOSITORY.UtilisateurRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partenaires")
public class PartenaireController {

    private final PartenaireRepository repo;

    // CONSTRUCTEUR 
    public PartenaireController(PartenaireRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public Partenaire create(@RequestBody Partenaire p) {
        return repo.save(p);
    }

    @GetMapping
    public List<Partenaire> list() {
        return repo.findAll();
    }
}

