package FLCS.GESTION.CONFIG;

import FLCS.GESTION.ENTITEES.Role;
import FLCS.GESTION.ENTITEES.Utilisateur;
import FLCS.GESTION.REPOSITORY.UtilisateurRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initUsers(
            UtilisateurRepository repo,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            // éviter les doublons
            if (repo.findByUsername("admin").isEmpty()) {

                Utilisateur admin = Utilisateur.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin123")) // ENCODAGE ICI
                        .role(Role.ADMIN)
                        .build();

                repo.save(admin);

                System.out.println("Admin créé : admin / admin123");
            }
        };
    }
}
