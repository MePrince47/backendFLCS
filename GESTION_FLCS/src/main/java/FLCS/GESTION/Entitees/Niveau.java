package FLCS.GESTION.Entitees;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente un niveau d'enseignement (ex: A1, A2, B1...).
 * Contient la capacité maximale et la liste des élèves affectés.
 */
@Entity
@Table(name = "niveaux")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "rentree", "eleves", "enseignant", "evaluations", "endpruefungen" })
public class Niveau extends BaseEntity {

    @Column(nullable = false, length = 10)
    private String nom; // A1, A2, B1, B2, C1

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TypeNiveau type = TypeNiveau.RENTREE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rentree_id")
    private Rentree rentree;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enseignant_id")
    private Enseignant enseignant;

    private String salle;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Statut statut = Statut.PLANIFIE;

    @OneToMany(mappedBy = "niveau", cascade = CascadeType.ALL)
    private List<Eleve> eleves = new ArrayList<>();

    @OneToMany(mappedBy = "niveau", cascade = CascadeType.ALL)
    private List<EvaluationHebdomadaire> evaluations = new ArrayList<>();

    @OneToMany(mappedBy = "niveau", cascade = CascadeType.ALL)
    private List<Endpruefung> endpruefungen = new ArrayList<>();

    @Column(name = "capacite_max")
    private Integer capaciteMax = 25;

    public enum TypeNiveau {
        RENTREE, INDEPENDANT
    }

    public enum Statut {
        PLANIFIE, EN_COURS, TERMINE, ANNULE
    }

    public boolean estComplet() {
        if (this.eleves == null)
            return false;
        int capacite = (this.capaciteMax != null) ? this.capaciteMax : 25;
        return eleves.size() >= capacite;
    }

}