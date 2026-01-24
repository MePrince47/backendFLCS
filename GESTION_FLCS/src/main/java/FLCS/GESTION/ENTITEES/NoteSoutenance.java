package FLCS.GESTION.ENTITEES;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class NoteSoutenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Eleve eleve;

    @ManyToOne(optional = false)
    private Niveau niveau;

    // Note sur 20
    private Double note;
}
