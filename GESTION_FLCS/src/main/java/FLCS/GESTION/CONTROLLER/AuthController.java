package FLCS.GESTION.CONTROLLER;

import FLCS.GESTION.DTO.LoginRequest;
import FLCS.GESTION.SERVICE.AuthService;
import FLCS.GESTION.ENTITEES.Utilisateur;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    // CONSTRUCTEUR 
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Utilisateur login(@RequestBody LoginRequest request) {
        // EXTRACTION DES CHAMPS
        String username = request.username();
        String password = request.password();

        // APPEL DU SERVICE
        return authService.login(username, password);
    }
}
