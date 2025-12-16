package FLCS.GESTION.ENTITEES;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data              // Génère getters, setters, toString, equals, hashCode
@NoArgsConstructor // Génère un constructeur sans arguments
@AllArgsConstructor // Génère un constructeur avec tous les arguments
@Builder           // Permet le pattern Builder pour créer des objets facilement
@Table(
  uniqueConstraints = @UniqueConstraint(
    columnNames = {"evaluation_hebdo_id", "eleve_id"}
  )
)
public class NoteHebdo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private EvaluationHebdo evaluationHebdo;

    @ManyToOne
    private Eleve eleve;

    private Double les;
    private Double hor;
    private Double schreib;
    private Double gramm;
    private Double spre;
}

