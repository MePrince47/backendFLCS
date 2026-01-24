package FLCS.GESTION.DTO;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record EleveRequest(

    @NotBlank String nom,
    @NotBlank String prenom,

    @NotNull @Past
    LocalDate dateNaiss,
    @NotBlank String lieuNaiss,

    @NotBlank String niveauScolaire,
    @NotBlank String typeProcedure,
    @NotNull Double montantProcedure,
    @NotBlank String telCandidat,
    @NotBlank String telParent,

    // références simples
    @NotBlank String codeNiveau,
    @NotBlank String nomPartenaire,
    String nomRentree  // peut etre null
) {}
