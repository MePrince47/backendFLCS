package FLCS.GESTION.CONTROLLER;

import FLCS.GESTION.ENTITEES.Utilisateur;
import FLCS.GESTION.DTO.UtilisateurResponse;
import FLCS.GESTION.SERVICE.UtilisateurService;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;

@Tag(
    name = "Utilisateurs",
    description = "Gestion des utilisateurs et des rôles de sécurité"
)
@RestController
@RequestMapping("/api/utilisateurs")
@PreAuthorize("hasRole('ADMIN')")
public class UtilisateurController {

    private final UtilisateurService service;

    public UtilisateurController(UtilisateurService service) {
        this.service = service;
    }

    @Operation(
        summary = "Créer un utilisateur",
        description = "Permet à l’administrateur de créer un nouvel utilisateur"
    )
    @ApiResponse(responseCode = "200", description = "Utilisateur créé")
    @PostMapping
    public Utilisateur creer(@RequestBody Utilisateur u) {
        return service.creerUtilisateur(u);
    }

    @Operation(
        summary = "Lister les utilisateurs",
        description = "Retourne la liste des utilisateurs avec leurs rôles"
    )
    @ApiResponse(responseCode = "200", description = "Liste des utilisateurs")
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

