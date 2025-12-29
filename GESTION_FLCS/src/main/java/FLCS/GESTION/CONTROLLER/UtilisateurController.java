package FLCS.GESTION.CONTROLLER;

import FLCS.GESTION.ENTITEES.Utilisateur;
import FLCS.GESTION.DTO.UtilisateurResponse;
import FLCS.GESTION.SERVICE.UtilisateurService;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
@PreAuthorize("hasRole('ADMIN')")
public class UtilisateurController {

    private final UtilisateurService service;

    public UtilisateurController(UtilisateurService service) {
        this.service = service;
    }

    @PostMapping
    public Utilisateur creer(@RequestBody Utilisateur u) {
        return service.creerUtilisateur(u);
    }

    @GetMapping
    public List<UtilisateurResponse> getAll() {
        return service.getAll()
           .stream()
           .map(u -> new UtilisateurResponse(
               u.getId(),
               u.getUsername(),
               u.getRole()))
           .toList();
    }

}

