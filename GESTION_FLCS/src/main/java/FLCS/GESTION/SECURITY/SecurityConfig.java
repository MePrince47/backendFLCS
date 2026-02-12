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
//@EnableMethodSecurity
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
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(csrf -> csrf.disable()) 
        .authorizeHttpRequests(auth -> auth
            // On autorise explicitement les patterns statiques de Swagger au cas où
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()
            .anyRequest().permitAll() 
        )
        // Désactive temporairement httpBasic si tu ne veux aucune demande de login
        // .httpBasic(Customizer.withDefaults()); 
        
        // Optionnel : autoriser les frames pour la console H2 si tu l'utilises
        .headers(headers -> headers.frameOptions(frame -> frame.disable()));

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
    
    // 1. Autorise les credentials (cookies, auth basic)
    config.setAllowCredentials(true);
    
    // 2. Origines statiques (ne pas utiliser "*" si allowCredentials est true)
    config.setAllowedOrigins(Arrays.asList(
        "https://www.flcs-center.com",
        "https://flcs-center.com",
        "http://localhost:4200"
    ));
    
    // 3. Méthodes autorisées
    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
    
    // 4. Headers autorisés (ceux que le navigateur a le droit d'envoyer)
    config.setAllowedHeaders(Arrays.asList(
        "Authorization",
        "Content-Type",
        "Accept",
        "Origin",
        "X-Requested-With",
        "Access-Control-Request-Method",
        "Access-Control-Request-Headers"
    ));
    
    // 5. Headers exposés (ceux que le front-end a le droit de lire)
    config.setExposedHeaders(Arrays.asList(
        "Authorization", 
        "Content-Type"
    ));

    // 6. Cache des requêtes OPTIONS (Preflight) pour 1 heure
    config.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config); 
    return source;
}


}