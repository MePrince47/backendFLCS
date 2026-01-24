package FLCS.GESTION.ENTITEES;


import jakarta.persistence.*;
import lombok.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Niveau {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private Integer bareme;

    // Fermeture d'un niveau
    @Builder.Default
    private boolean cloture = false;


    @OneToMany(mappedBy = "niveau", cascade = CascadeType.ALL)
    @JsonIgnore 
    private List<EvaluationHebdo> evaluationsHebdo;

    @OneToOne(mappedBy = "niveau", cascade = CascadeType.ALL)
    @JsonIgnore
    private Endprufung endprufung;

    @ManyToOne(optional = true)
    @JoinColumn(nullable = true)
    @JsonBackReference 
    private Rentree rentree;
}

