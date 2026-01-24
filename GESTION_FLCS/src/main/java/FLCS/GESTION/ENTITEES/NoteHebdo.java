package FLCS.GESTION.ENTITEES;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"evaluation_hebdo_id", "eleve_id"}
    )
)
public class NoteHebdo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "evaluation_hebdo_id", nullable = false)
    @NotNull
    private EvaluationHebdo evaluationHebdo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "eleve_id", nullable = false)
    @NotNull
    private Eleve eleve;

    @NotNull 
    private Double les;

    @NotNull 
    private Double hor;

    @NotNull  
    private Double schreib;

    @NotNull 
    private Double gramm;

    @NotNull 
    private Double spre;

}


