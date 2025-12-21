package FLCS.GESTION.Dtos.Request;
import lombok.Data;
// import javax.validation.constraints.*;
import java.time.LocalDate;
@Data
public class NiveauRequest {

    // @NotBlank(message = "Le nom du niveau est obligatoire")
    // @Pattern(regexp = "^(A1|A2|B1|B2|C1)$", message = "Le niveau doit être A1, A2, B1, B2 ou C1")
    private String nom;
    
    private String type = "RENTREE";
    
    // @NotNull(message = "La rentrée est obligatoire")
    private Long rentreeId;
    
    private Long enseignantId;
    
    private String salle;
    
    private LocalDate dateDebut;
    
    private LocalDate dateFin;

}
