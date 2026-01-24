package FLCS.GESTION.ENTITEES;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;


import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rentree {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomRentree;

    @Column(nullable = false)
    private LocalDate dateDebut;

    @OneToMany(mappedBy = "rentree", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    private List<Niveau> niveaux = new ArrayList<>();

    public void addNiveau(Niveau niveau) {
        niveaux.add(niveau);
        niveau.setRentree(this);
    }
}




