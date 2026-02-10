package FLCS.GESTION.SECURITY;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
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
            // 1. Activation du CORS avec la configuration définie plus bas
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // 2. Désactivation du CSRF (nécessaire pour les APIs REST stateless)
            .csrf(csrf -> csrf.disable())
            
            // 3. Gestion des autorisations
            .authorizeHttpRequests(auth -> auth
                // Si tu veux rendre les listes publiques, décommente les lignes suivantes :
                // .requestMatchers("/api/partenaires/**").permitAll()
                // .requestMatchers("/api/rentrees/**").permitAll()
                .anyRequest().authenticated()
            )
            
            // 4. Authentification Basic (pour ton admin:admin123)
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

    // --- Configuration CORS Centralisée ---
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        
        // Autorise l'envoi des headers d'authentification (Basic Auth)
        config.setAllowCredentials(true); 
        
        // Liste exacte des origines autorisées (Frontend)
        config.setAllowedOrigins(Arrays.asList(
            "http://localhost:4200",
            "https://flcs-center.com",
            "https://www.flcs-center.com"
        ));
        
        // Autorise tous les headers standards et l'Authorization
        config.setAllowedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "X-Requested-With",
            "Accept",
            "Origin",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers"
        ));
        
        // Méthodes HTTP autorisées
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // Durée de mise en cache de la réponse CORS (Preflight)
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}