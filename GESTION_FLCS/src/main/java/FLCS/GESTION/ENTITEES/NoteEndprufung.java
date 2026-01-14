package FLCS.GESTION.ENTITEES;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"endprufung_id", "eleve_id"}
    )
)
public class NoteEndprufung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "endprufung_id", nullable = false)
    @NotNull
    private Endprufung endprufung;

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


