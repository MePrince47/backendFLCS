package FLCS.GESTION.DTO;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record EleveRequest(

    @NotBlank String nom,
    @NotBlank String prenom,

    @NotNull @Past
    LocalDate dateNaiss,

    @NotBlank String niveauScolaire,
    @NotBlank String typeProcedure,
    @NotBlank String telCandidat,
    @NotBlank String telParent,
    @NotBlank String statut,

    // références simples
    @NotBlank String codeNiveau,
    @NotBlank String nomPartenaire,
    @NotBlank String nomRentree
) {}
