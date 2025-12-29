package FLCS.GESTION.ENTITEES;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;


@Entity
@Data              // Génère getters, setters, toString, equals, hashCode
@NoArgsConstructor // Génère un constructeur sans arguments
@AllArgsConstructor // Génère un constructeur avec tous les arguments
@Builder           // Permet le pattern Builder pour créer des objets facilement
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
    if(this.niveaux == null){
        this.niveaux = new ArrayList<>();
    }
    this.niveaux.add(niveau);
    niveau.setRentree(this);
}

}




