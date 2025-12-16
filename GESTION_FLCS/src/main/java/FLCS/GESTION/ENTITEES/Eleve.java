package FLCS.GESTION.ENTITEES;


import jakarta.persistence.*;
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

    private String nom;
    private String prenom;
    private LocalDate dateNaiss;

    private String niveauScolaire;
    private String niveauLangue;
    private String typeProcedure;
    private String telCandidat;
    private String telParent;
    private String statut;

    @ManyToOne
    private Partenaire partenaire;

    @ManyToOne
    private Rentree rentree;
}
