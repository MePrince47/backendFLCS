package FLCS.GESTION.SECURITY;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // Indispensable pour OPTIONS
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
            // 1. Appliquer la config CORS définie plus bas
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // 2. DÉSACTIVER LE CSRF (C'est ce qui bloque tes POST en 403)
            .csrf(csrf -> csrf.disable()) 
            
            // 3. Gestion des autorisations
            .authorizeHttpRequests(auth -> auth
                // Autoriser les requêtes "Preflight" (OPTIONS) du navigateur sans login
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() 
                
                // Si tu as des routes d'auth publiques (ex: login), ajoute-les ici :
                // .requestMatchers("/api/auth/**").permitAll()
                
                // Tout le reste demande d'être connecté
                .anyRequest().authenticated()
            )
            
            // 4. Activer le Basic Auth (ton login:password envoyé par Angular)
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
        
        config.setAllowCredentials(true); 
        
        // Origines autorisées (Frontend)
        config.setAllowedOrigins(Arrays.asList(
            "http://localhost:4200",
            "https://flcs-center.com",
            "https://www.flcs-center.com"
        ));
        
        // Headers autorisés (Authorization est crucial pour ton Basic Auth)
        config.setAllowedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "X-Requested-With",
            "Accept",
            "Origin"
        ));
        
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}