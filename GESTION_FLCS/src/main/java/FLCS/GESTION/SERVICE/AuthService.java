package FLCS.GESTION.SERVICE;

import FLCS.GESTION.ENTITEES.Utilisateur;
import FLCS.GESTION.REPOSITORY.UtilisateurRepository;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UtilisateurRepository repo;

    public AuthService(UtilisateurRepository repo) {
        this.repo = repo;
    }

    public Utilisateur login(String username, String password) {
        Utilisateur u = repo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur inexistant"));

        if (!u.getPassword().equals(password)) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        return u;
    }
}
