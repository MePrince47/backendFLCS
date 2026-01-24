package FLCS.GESTION.ENTITEES;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Resultat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Eleve eleve;

    @ManyToOne
    private Niveau niveau;

    // Moyennes finales
    private Double moyLes;
    private Double moyHor;
    private Double moySchreib;
    private Double moyGramm;
    private Double moySpre;

    // Notes Endprüfung
    private Double endLes;
    private Double endHor;
    private Double endSchreib;
    private Double endGramm;
    private Double endSpre;

    // Notes Soutenance
    private Double soutenance;

    private Double moyenneGenerale;
    private boolean admis;
}

