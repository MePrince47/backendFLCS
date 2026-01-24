package FLCS.GESTION.ENTITEES;


import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"niveau_id", "semaine_num"}
    )
)
public class EvaluationHebdo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(1)
    @Max(7)
    private Integer semaineNum;

    @ManyToOne(optional = false)
    @JoinColumn(name = "niveau_id", nullable = false)
    @JsonIgnore
    private Niveau niveau;

    @OneToMany(mappedBy = "evaluationHebdo", cascade = CascadeType.ALL)
    private List<NoteHebdo> notes;
}



