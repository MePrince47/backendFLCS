package FLCS.GESTION.ENTITEES;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data              // Génère getters, setters, toString, equals, hashCode
@NoArgsConstructor // Génère un constructeur sans arguments
@AllArgsConstructor // Génère un constructeur avec tous les arguments
@Builder           // Permet le pattern Builder pour créer des objets facilement
public class Eleve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    // format YYYY-MM-DD
    @NotNull(message = "La date de naissance est obligatoire")
    @Past(message = "La date de naissance doit être dans le passé")
    private LocalDate dateNaiss;
    
    @NotBlank
    private String niveauScolaire;

    @NotBlank
    private String typeProcedure;
    @NotBlank
    private String telCandidat;
    @NotBlank
    private String telParent;
    @NotBlank
    private String statut;

    @ManyToOne
    private Partenaire partenaire;

    @ManyToOne
    private Rentree rentree;

    @ManyToOne
    @JoinColumn(name = "niveau_id")
    private Niveau niveauLangue;

}
