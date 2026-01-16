package FLCS.GESTION.CONFIG;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "FLCS – Système de Gestion Scolaire",
        version = "1.0",
        description = "API REST pour la gestion des élèves, notes, résultats, paiements et progression académique",
        contact = @Contact(
            name = "FLCS Team"
        ),
        license = @License(
            name = "Usage académique"
        )
    )
)
public class OpenApiConfig {
}
