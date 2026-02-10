package FLCS.GESTION.SECURITY;

import java.util.Arrays;
import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
@EnableWebSecurity
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
            // 1. CORS en premier !
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // 2. Désactivation du CSRF (Indispensable pour les POST API)
            .csrf(csrf -> csrf.disable()) 
            
            // 3. Autorisations
            .authorizeHttpRequests(auth -> auth
                // On autorise TOUTES les requêtes OPTIONS sans authentification
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() 
                // On laisse passer l'accès aux ressources statiques si besoin
                .anyRequest().authenticated()
            )
            
            // 4. Authentification Basic
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
        
        // Autorise les credentials (nécessaire pour le header Authorization)
        config.setAllowCredentials(true); 
        
        // Utilisation de OriginPatterns (plus souple pour matcher www et non-www)
        config.setAllowedOriginPatterns(Arrays.asList(
            "https://www.flcs-center.com",
            "https://flcs-center.com",
            "http://localhost:4200"
        ));
        
        // On autorise TOUS les headers pour éviter le "Invalid CORS request"
        config.setAllowedHeaders(Arrays.asList("*"));
        
        // Méthodes autorisées
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // Exposer le header Authorization pour Angular
        config.setExposedHeaders(Collections.singletonList("Authorization"));
        
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}