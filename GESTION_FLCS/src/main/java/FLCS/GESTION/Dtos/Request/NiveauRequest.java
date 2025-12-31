package FLCS.GESTION.Dtos.Request;

import lombok.Data;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Data
public class NiveauRequest {

    @NotBlank(message = "Le nom du niveau est obligatoire")
    @Pattern(regexp = "^(A1|A2|B1|B2|C1)$", message = "Le niveau doit être A1, A2, B1, B2 ou C1")
    private String nom;

    private String type = "RENTREE";

    @NotNull(message = "La rentrée est obligatoire")
    private Long rentreeId;

    @NotNull(message = "L'enseignant est obligatoire")
    private Long enseignantId;

    @NotNull(message = "La salle est obligatoire")
    private String salle;

    @NotNull(message = "La date de debut est obligatoire")
    private LocalDate dateDebut;

    @NotNull(message = "La date de fin est obligatoire")
    private LocalDate dateFin;

    private Integer capaciteMax;

}
