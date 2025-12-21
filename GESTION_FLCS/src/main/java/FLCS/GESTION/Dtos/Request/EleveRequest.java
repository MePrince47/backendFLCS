package FLCS.GESTION.Dtos.Request;
import lombok.*;
import java.time.LocalDate;

import jakarta.validation.constraints.*;
@Data
public class EleveRequest {
    @NotBlank(message = "Le matricule est obligatoire")
    private String matricule;
    
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;
    
    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;
    
    private LocalDate dateNaissance;
    
    @Pattern(regexp = "^\\+?[0-9\\s\\-]{8,20}$", message = "Format de téléphone invalide")
    private String telephone;
    
    private String telephoneParent;
    
    @Email(message = "Format d'email invalide")
    private String email;
    
    private String niveauScolaire;
    
    private String typeProcedure;
    
    @NotNull(message = "La rentrée est obligatoire")
    private Long rentreeId;
    
    @NotNull(message = "Le niveau est obligatoire")
    private Long niveauId;
    
    private Long partenaireId;
    
    @DecimalMin(value = "0.0", message = "Le montant total requis ne peut être négatif")
    private Double montantTotalRequis ;
}
