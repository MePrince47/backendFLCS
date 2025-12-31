package FLCS.GESTION.Dtos.Request;
import lombok.*;
import jakarta.validation.constraints.*;
@Data
public class EvaluationHebdomadaireRequest {
    @NotNull(message = "L'élève est obligatoire")
    private Long eleveId;
    
    @NotNull(message = "Le niveau est obligatoire")
    private Long niveauId;
    
    @NotNull(message = "L'enseignant est obligatoire")
    private Long enseignantId;
    
    @Min(value = 1, message = "La semaine doit être entre 1 et 52")
    @Max(value = 52, message = "La semaine doit être entre 1 et 52")
    private Integer semaine;
    
    @Min(value = 2020, message = "L'année doit être 2020 ou ultérieure")
    @Max(value = 2100, message = "L'année ne peut dépasser 2100")
    private Integer annee;
    
    @DecimalMin(value = "0.0", message = "La note Lesen doit être entre 0 et 20")
    @DecimalMax(value = "20.0", message = "La note Lesen doit être entre 0 et 20")
    private Double noteLesen;
    
    @DecimalMin(value = "0.0", message = "La note Hören doit être entre 0 et 20")
    @DecimalMax(value = "20.0", message = "La note Hören doit être entre 0 et 20")
    private Double noteHoren;
    
    @DecimalMin(value = "0.0", message = "La note Schreiben doit être entre 0 et 20")
    @DecimalMax(value = "20.0", message = "La note Schreiben doit être entre 0 et 20")
    private Double noteSchreiben;
    
    @DecimalMin(value = "0.0", message = "La note Grammatik doit être entre 0 et 20")
    @DecimalMax(value = "20.0", message = "La note Grammatik doit être entre 0 et 20")
    private Double noteGrammatik;
    
    @DecimalMin(value = "0.0", message = "La note Sprechen doit être entre 0 et 20")
    @DecimalMax(value = "20.0", message = "La note Sprechen doit être entre 0 et 20")
    private Double noteSprechen;

    public Object getNumeroLecon() {
        
        throw new UnsupportedOperationException("Unimplemented method 'getNumeroLecon'");
    }


    public Object getDateEvaluation() {
       
        throw new UnsupportedOperationException("Unimplemented method 'getDateEvaluation'");
    }

}
