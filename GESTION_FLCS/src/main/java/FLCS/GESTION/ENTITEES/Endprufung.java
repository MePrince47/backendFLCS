package FLCS.GESTION.ENTITEES;


import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"niveau_id"}
    )
)
public class Endprufung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La date de l'examen est obligatoire")
    private LocalDate dateExam;

    @OneToOne(optional = false)
    @JoinColumn(name = "niveau_id", nullable = false)
    @NotNull
    private Niveau niveau;

    //  AJOUT
    @OneToMany(mappedBy = "endprufung", cascade = CascadeType.ALL)
    private List<NoteEndprufung> notes;
}


