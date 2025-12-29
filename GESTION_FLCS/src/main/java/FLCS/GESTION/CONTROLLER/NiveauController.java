package FLCS.GESTION.CONTROLLER;

import FLCS.GESTION.ENTITEES.Niveau;
import FLCS.GESTION.REPOSITORY.NiveauRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/niveaux")
public class NiveauController {

    private final NiveauRepository repo;

    // CONSTRUCTEUR 
    public NiveauController(NiveauRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public Niveau create(@RequestBody Niveau n) {
        return repo.save(n);
    }

    @GetMapping
    public List<Niveau> list() {
        return repo.findAll();
    }
}

