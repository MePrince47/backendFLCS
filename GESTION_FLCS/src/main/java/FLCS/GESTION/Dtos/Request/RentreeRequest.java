// DTOs (Data Transfer Objects)


package FLCS.GESTION.Dtos.Request;
import java.time.LocalDate;

import lombok.*;
// import jakarta.persistence.*;


@Data
public class RentreeRequest {
    // @NotBlank(message="le code est obligatoire")
    // @Size(max=50,message="minimun 10 caracteres")
    private String code;
// @NotBlank(message="le code est obligatoire")
    private String nom;

    private String description;
// @NotBlank(message="le code est obligatoire")
    // @FutureOrPresent(message = "La date de début doit être aujourd'hui ou dans le futur")

    private LocalDate dateDebut;

    // @NotBlank(message="le code est obligatoire")
    
    // @Future(message = "La date de fin doit être dans le futur")
    private LocalDate dateFin;

    // @Min(value = 1, message = "Le nombre de places doit être au moins 1")
    private Integer nombrePlacesMax = 30;

    




}
