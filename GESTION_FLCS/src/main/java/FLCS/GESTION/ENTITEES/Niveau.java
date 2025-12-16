package FLCS.GESTION.ENTITEES;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data              // Génère getters, setters, toString, equals, hashCode
@NoArgsConstructor // Génère un constructeur sans arguments
@AllArgsConstructor // Génère un constructeur avec tous les arguments
@Builder           // Permet le pattern Builder pour créer des objets facilement
public class Niveau {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomNiveau; // A1, A2, B1, B2, C1
    private String typeNiveau; // RENTREE / INDEPENDANT

    @ManyToOne
    private Rentree rentree;
}

