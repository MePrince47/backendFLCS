package FLCS.GESTION.SECURITY;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // IMPORT MANQUANT AJOUTÉ
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity // Ajouté pour garantir l'activation de la config
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Appliquer la config CORS définie plus bas
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // 2. Désactiver le CSRF pour les API REST (nécessaire pour POST/PUT/DELETE)
            .csrf(csrf -> csrf.disable())
            
            // 3. Gestion des autorisations
            .authorizeHttpRequests(auth -> auth
                // Autoriser explicitement les requêtes de pré-vérification du navigateur
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() 
                
                // Autoriser l'accès aux routes publiques (ex: authentification) si besoin
                // .requestMatchers("/api/auth/**").permitAll()
                
                // Tout le reste nécessite d'être authentifié via Basic Auth
                .anyRequest().authenticated()
            )
            
            // 4. Utiliser l'authentification Basic (ce que tu envoies via Angular)
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        
        // Autorise l'envoi des headers d'authentification (Basic Auth)
        config.setAllowCredentials(true); 
        
        // Liste exacte des origines autorisées
        config.setAllowedOrigins(Arrays.asList(
            "http://localhost:4200",
            "https://flcs-center.com",
            "https://www.flcs-center.com"
        ));
        
        // Headers autorisés (Indispensable pour recevoir 'Authorization' d'Angular)
        config.setAllowedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "X-Requested-With",
            "Accept",
            "Origin",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers"
        ));
        
        // Méthodes autorisées
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // Cache pour 1h (évite de renvoyer OPTIONS à chaque clic)
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}