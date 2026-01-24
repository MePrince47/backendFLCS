package FLCS.GESTION.SERVICE;

import FLCS.GESTION.ENTITEES.Utilisateur;
import FLCS.GESTION.REPOSITORY.UtilisateurRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UtilisateurService {

    private final UtilisateurRepository repo;
    private final PasswordEncoder encoder;

    public UtilisateurService(UtilisateurRepository repo,
                              PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public Utilisateur creerUtilisateur(Utilisateur user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }

    public List<Utilisateur> getAll() {
        return repo.findAll();
    }
}
